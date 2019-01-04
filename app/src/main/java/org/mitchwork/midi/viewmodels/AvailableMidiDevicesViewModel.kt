package org.mitchwork.midi.viewmodels

import android.bluetooth.le.ScanResult
import android.media.midi.*
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import org.mitchwork.midi.data.MidiDeviceTracker

class AvailableMidiDevicesViewModel(private val midiManager: MidiManager) : ViewModel() {
    val availableDevices = AvailableMidiDevices(midiManager)

    fun refreshDevices() = availableDevices.refresh()

    val midiDevice: MediatorLiveData<MidiDevice> = MediatorLiveData()

    val midiInputPort: MediatorLiveData<MidiInputPort> = MediatorLiveData()

    val midiOutputPort: MediatorLiveData<MidiOutputPort> = MediatorLiveData()

    fun addBluetoothResult(scanResult: ScanResult) = availableDevices.addBluetoothDevice(scanResult)

    fun openMidiDevice(device: MidiDeviceTracker) {
        midiDevice.addSource(MidiDeviceLiveData(midiManager, device)) {
            midiDevice.value = it
        }
    }

    fun openFirstInputPort() {
        if (midiInputPort.value != null) {
            return
        }
        if (midiInputPort.value == null) {
            midiInputPort.removeSource(midiDevice)
        }
        midiInputPort.addSource(midiDevice) {
            device ->
            val portNumber =
                device.info.ports.find { it.type == MidiDeviceInfo.PortInfo.TYPE_INPUT }?.portNumber ?: -1
            if (portNumber != -1) {
                midiInputPort.value = device.openInputPort(portNumber)
            }
        }
    }

    fun openFirstOutputPort() {
        if (midiOutputPort.value != null) {
            return
        }
        if (midiOutputPort.value == null) {
            midiOutputPort.removeSource(midiDevice)
        }
        midiOutputPort.addSource(midiDevice) {
                device ->
            val portNumber =
                device.info.ports.find { it.type == MidiDeviceInfo.PortInfo.TYPE_OUTPUT }?.portNumber ?: -1
            if (portNumber != -1) {
                midiOutputPort.value = device.openOutputPort(portNumber)
            }
        }
    }

    class MidiDeviceLiveData(midiManager: MidiManager, deviceTracker: MidiDeviceTracker) : LiveData<MidiDevice>() {
        init {
            val handler = Handler(Looper.getMainLooper())
            if (deviceTracker.midiDevice != null) {
                midiManager.openDevice(
                    deviceTracker.midiDevice,
                    { device ->
                        value?.close()
                        value = device
                    }, handler
                )
            } else if (deviceTracker.bluetoothDevice != null) {
                midiManager.openBluetoothDevice(
                    deviceTracker.bluetoothDevice.device,
                    { device ->
                        value?.close()
                        value = device

                    }, handler
                )
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        midiDevice.value?.close()
    }
}
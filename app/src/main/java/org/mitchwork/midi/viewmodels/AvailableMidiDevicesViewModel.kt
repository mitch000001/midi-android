package org.mitchwork.midi.viewmodels

import android.bluetooth.le.ScanResult
import android.media.midi.MidiDevice
import android.media.midi.MidiDeviceInfo
import android.media.midi.MidiInputPort
import android.media.midi.MidiManager
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import org.mitchwork.midi.data.MidiDeviceTracker

class AvailableMidiDevicesViewModel(private val midiManager: MidiManager) : ViewModel() {
    val availableDevices = AvailableMidiDevices(midiManager)

    fun refreshDevices() = availableDevices.refresh()

    var midiDevice: MediatorLiveData<MidiDevice> = MediatorLiveData()

    var midiInputPort: MediatorLiveData<MidiInputPort> = MediatorLiveData()

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

    class MidiDeviceLiveData(midiManager: MidiManager, device: MidiDeviceTracker) : LiveData<MidiDevice>() {
        init {
            val handler = Handler(Looper.getMainLooper())
            if (device.midiDevice != null) {
                midiManager.openDevice(
                    device.midiDevice,
                    { device ->
                        value?.close()
                        value = device
                    }, handler
                )
            } else if (device.bluetoothDevice != null) {
                midiManager.openBluetoothDevice(
                    device.bluetoothDevice.device,
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
        midiDevice?.value?.close()
    }
}
package org.mitchwork.midi.viewmodels

import android.bluetooth.le.ScanResult
import android.media.midi.MidiDevice
import android.media.midi.MidiManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import org.mitchwork.midi.data.MidiDeviceTracker

class AvailableMidiDevicesViewModel(private val midiManager: MidiManager) : ViewModel() {
    val availableDevices = AvailableMidiDevices(midiManager)

    var midiDevice: LiveData<MidiDevice>? = null

    fun addBluetoothResult(scanResult: ScanResult) = availableDevices.addBluetoothDevice(scanResult)

    fun openMidiDevice(device: MidiDeviceTracker) {
        midiDevice = MidiDeviceLiveData(midiManager, device)
    }

    class MidiDeviceLiveData(midiManager: MidiManager, device: MidiDeviceTracker) : LiveData<MidiDevice>() {
        init {
            if (device.midiDevice != null) {
                midiManager.openDevice(device.midiDevice,
                    { device -> value = device }, null)
            } else if (device.bluetoothDevice != null) {
                midiManager.openBluetoothDevice(device.bluetoothDevice.device,
                    { device -> value = device }, null)
            }
        }
    }
}
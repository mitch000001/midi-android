package org.mitchwork.midi.viewmodels

import android.bluetooth.le.ScanResult
import android.media.midi.MidiDeviceInfo
import android.media.midi.MidiManager
import androidx.lifecycle.LiveData
import org.mitchwork.midi.data.MidiDeviceTracker

class AvailableMidiDevices(
    private val midiManager: MidiManager
) : LiveData<List<MidiDeviceTracker>>() {
    val callback: MidiManager.DeviceCallback

    init {
        value = midiManager.devices.map { it -> MidiDeviceTracker(it, null) }
        callback = object : MidiManager.DeviceCallback() {
            override fun onDeviceRemoved(device: MidiDeviceInfo?) {
                if (device == null) return
                value = value?.filterNot { it.midiDevice?.id == device.id }
            }

            override fun onDeviceAdded(device: MidiDeviceInfo?) {
                if (device != null) {
                    value = value?.plus(MidiDeviceTracker(device, null))
                }
            }
        }
    }

    fun addBluetoothDevice(scanResult: ScanResult) {
        value = value?.plus(MidiDeviceTracker(null, scanResult))
    }

    override fun onInactive() {
        midiManager.unregisterDeviceCallback(callback)
    }

    override fun onActive() {
        midiManager.registerDeviceCallback(callback, null)
    }

}
package org.mitchwork.midi.viewmodels

import android.bluetooth.BluetoothDevice
import android.media.midi.MidiDevice
import android.media.midi.MidiManager
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.ViewModel

class BluetoothViewModel(private val midiManager: MidiManager) : ViewModel() {

    lateinit var midiDevice: MidiDevice

    fun connectToBluetoothMidiDevice(device: BluetoothDevice) {
        midiManager.openBluetoothDevice(device, {
            midiDevice = midiDevice
        }, Handler(Looper.getMainLooper()))
    }

}
package org.mitchwork.midi.data

import android.bluetooth.le.ScanResult
import android.media.midi.MidiDeviceInfo
import android.os.Build

data class MidiDeviceTracker(
    val midiDevice: MidiDeviceInfo?,
    val bluetoothDevice: ScanResult?
) {
    val id: Int
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            midiDevice?.id ?: bluetoothDevice?.advertisingSid ?: 0
        } else {
            midiDevice?.id ?: bluetoothDevice?.hashCode() ?: 0
        }
}

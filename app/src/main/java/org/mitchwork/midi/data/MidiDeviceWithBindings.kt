package org.mitchwork.midi.data

import androidx.room.Embedded
import androidx.room.Relation

class MidiDeviceWithBindings {
    @Embedded
    lateinit var device: MidiDevice

    @Relation(parentColumn = "uid", entityColumn = "midi_device_id")
    var controlChanges: List<ControlChange> = arrayListOf()
}
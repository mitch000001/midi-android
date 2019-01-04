package org.mitchwork.midi.data

import androidx.room.ColumnInfo
import androidx.room.Embedded

class ControlChangeWithMidiChannel {
    @Embedded
    lateinit var controlChange: ControlChange

    @ColumnInfo(name = "midi_channel") var midiChannel: Int = -1

}
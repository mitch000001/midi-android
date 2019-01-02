package org.mitchwork.midi.data

import androidx.room.*
import androidx.room.ForeignKey.CASCADE

@Entity(
    tableName = "program_changes",
    foreignKeys = [ForeignKey(
        entity = MidiDevice::class,
        parentColumns = arrayOf("uid"),
        childColumns = arrayOf("midi_device_id"),
        onUpdate = CASCADE,
        onDelete = CASCADE
    )],
    indices = [Index("midi_device_id")]
)
data class ProgramChange(
    @PrimaryKey var uid: String,
    @ColumnInfo(name = "value") var value: Int,
    @ColumnInfo(name = "midi_device_id") var midiDeviceID: String
)
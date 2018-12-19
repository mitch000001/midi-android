package org.mitchwork.midi.persistence

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity(
    tableName = "program_changes",
    foreignKeys = arrayOf(ForeignKey(
        entity = MidiDevice::class,
        parentColumns = arrayOf("uid"),
        childColumns = arrayOf("midi_device_id"),
        onUpdate = CASCADE,
        onDelete = CASCADE
    ))
)
data class ProgramChange(
    @PrimaryKey(autoGenerate = true) var uid: Int,
    @ColumnInfo(name = "value") var value: Int,
    @ColumnInfo(name = "midi_device_id") var midiDeviceID: Int
)
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
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "value") var value: Int
){
    @PrimaryKey var uid: String = ""
    @ColumnInfo(name = "midi_device_id") var midiDeviceID: String = ""

    fun display(): String = "$name (PC# $value)"
}
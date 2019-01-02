package org.mitchwork.midi.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "midi_devices")
data class MidiDevice(
    @PrimaryKey var uid: String,
    @ColumnInfo(name = "name") var name: String?,
    @ColumnInfo(name = "midi_channel") var midiChannel: Int?,
    @ColumnInfo(name = "image_url") var imageUrl: String?
)
package org.mitchwork.midi.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface MidiDeviceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDevice(vararg devices: MidiDevice)

    @Update
    fun updateDevice(vararg devices: MidiDevice)

    @Delete
    fun deleteDevice(vararg devices: MidiDevice)

    @Query("SELECT * FROM midi_devices")
    fun getAll(): LiveData<List<MidiDevice>>

    @Transaction
    @Query("SELECT * FROM midi_devices WHERE uid = :id")
    fun getDeviceWithBindingsByID(id: String): LiveData<MidiDeviceWithBindings>

    @Query("SELECT * FROM midi_devices WHERE uid = :id")
    fun getDeviceByID(id: String): LiveData<MidiDevice>

}
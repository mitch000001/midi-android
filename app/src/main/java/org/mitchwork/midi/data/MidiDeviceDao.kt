package org.mitchwork.midi.persistence

import androidx.lifecycle.MutableLiveData
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
    fun getAll(): MutableLiveData<List<MidiDevice>>
}
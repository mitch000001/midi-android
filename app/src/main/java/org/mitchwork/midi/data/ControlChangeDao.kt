package org.mitchwork.midi.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ControlChangeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertControlChanges(vararg controlChanges: ControlChange)

    @Update
    fun updateControlChanges(vararg controlChanges: ControlChange)

    @Delete
    fun deleteControlChanges(vararg controlChanges: ControlChange)

    @Query("SELECT * FROM control_changes")
    fun getAll(): LiveData<List<ControlChange>>

    @Query("SELECT * FROM control_changes WHERE midi_device_id = :deviceID")
    fun getAllForMidiDevice(deviceID: String): LiveData<List<ControlChange>>
}
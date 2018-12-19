package org.mitchwork.midi.persistence

import androidx.lifecycle.MutableLiveData
import androidx.room.*

@Dao
interface ProgramChangeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProgramChanges(vararg programChanges: ProgramChange)

    @Update
    fun updateProgramChanges(vararg programChanges: ProgramChange)

    @Delete
    fun deleteProgramChanges(vararg programChanges: ProgramChange)

    @Query("SELECT * FROM program_changes")
    fun getAll(): MutableLiveData<List<ProgramChange>>

    @Query("SELECT * FROM program_changes WHERE midi_device_id = :deviceID")
    fun getAllForMidiDevice(deviceID: Int): MutableLiveData<List<ProgramChange>>
}
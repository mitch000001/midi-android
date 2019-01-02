package org.mitchwork.midi.data

import android.media.midi.MidiManager
import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext

class MidiDeviceRepository private constructor(
    private val midiDeviceDao: MidiDeviceDao,
    private val controlChangeDao: ControlChangeDao,
    private val midiManager: MidiManager
) {

    fun getDevices() = midiDeviceDao.getAll()

    suspend fun save(device: MidiDevice)  {
        withContext(IO) {
            midiDeviceDao.insertDevice(device)
        }
    }

    fun getDeviceByID(id: String): LiveData<MidiDevice> = midiDeviceDao.getDeviceByID(id)

    fun getControlChangesForMidiDevice(deviceID: String) = controlChangeDao.getAllForMidiDevice(deviceID)

    suspend fun saveControlChange(cc: ControlChange) {
        withContext(IO) {
            controlChangeDao.insertControlChanges(cc)
        }
    }

    companion object {

        // For Singleton instantiation
        @Volatile private var instance: MidiDeviceRepository? = null

        fun getInstance(deviceDao: MidiDeviceDao, controlChangeDao: ControlChangeDao, midiManager: MidiManager) =
            instance ?: synchronized(this) {
                instance ?: MidiDeviceRepository(deviceDao, controlChangeDao, midiManager).also { instance = it }
            }
    }
}
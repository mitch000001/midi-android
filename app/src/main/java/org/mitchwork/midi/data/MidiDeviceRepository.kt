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

    fun getDeviceByID(id: String): LiveData<MidiDevice> {
        return midiDeviceDao.getDeviceByID(id)
    }

    fun getControlChangesForMidiDevice(deviceID: String): LiveData<List<ControlChangeWithMidiChannel>> {
        return controlChangeDao.getAllForMidiDevice(deviceID)
    }

    suspend fun saveControlChange(cc: ControlChange) {
        withContext(IO) {
            controlChangeDao.insertControlChanges(cc)
        }
    }

    suspend fun updateControlChange(controlChange: ControlChange) {
        withContext(IO) {
            controlChangeDao.updateControlChanges(controlChange)
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
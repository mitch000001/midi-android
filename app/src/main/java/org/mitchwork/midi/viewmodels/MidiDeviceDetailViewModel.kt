package org.mitchwork.midi.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.mitchwork.midi.data.ControlChange
import org.mitchwork.midi.data.MidiDevice
import org.mitchwork.midi.data.MidiDeviceRepository
import java.util.*

class MidiDeviceDetailViewModel internal constructor(
    private val midiDeviceRepository: MidiDeviceRepository,
    private val deviceID: String
)  : ViewModel() {

    val device: LiveData<MidiDevice> = midiDeviceRepository.getDeviceByID(deviceID)
    val controlChanges: LiveData<List<ControlChange>> = midiDeviceRepository.getControlChangesForMidiDevice(deviceID)

    private val viewModelJob = Job()
    private val viewModelScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    /**
     * Cancel all coroutines when the ViewModel is cleared.
     */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun saveControlChange(cc: ControlChange) {
        cc.uid = UUID.randomUUID().toString()
        cc.midiDeviceID = deviceID
        viewModelScope.launch {
            midiDeviceRepository.saveControlChange(cc)
        }
    }
}

package org.mitchwork.midi.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.mitchwork.midi.data.MidiDevice
import org.mitchwork.midi.data.MidiDeviceRepository
import java.util.*

class MidiDeviceEditViewModel internal constructor(
    val repository: MidiDeviceRepository
) : ViewModel() {
    var name: MutableLiveData<String> = MutableLiveData()
    var midiChannel: MutableLiveData<Int> = MutableLiveData()

    private val viewModelJob = Job()
    private val viewModelScope = CoroutineScope(Main + viewModelJob)

    /**
     * Cancel all coroutines when the ViewModel is cleared.
     */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun save() = viewModelScope.launch {
        repository.save(MidiDevice(UUID.randomUUID().toString(), name.value, midiChannel.value, null))
    }
}
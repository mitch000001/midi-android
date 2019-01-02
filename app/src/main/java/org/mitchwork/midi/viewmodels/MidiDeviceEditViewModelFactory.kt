package org.mitchwork.midi.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.mitchwork.midi.data.MidiDeviceRepository

class MidiDeviceEditViewModelFactory(
    private val repository: MidiDeviceRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) = MidiDeviceEditViewModel(repository) as T

}
package org.mitchwork.midi.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.mitchwork.midi.data.MidiDeviceRepository

class MidiDeviceDetailViewModelFactory(
    private val repository: MidiDeviceRepository,
    private val deviceID: String
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) = MidiDeviceDetailViewModel(repository, deviceID) as T

}
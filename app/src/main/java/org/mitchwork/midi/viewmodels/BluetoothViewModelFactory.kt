package org.mitchwork.midi.viewmodels

import android.media.midi.MidiManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class BluetoothViewModelFactory(
    private val midiManager: MidiManager
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) = BluetoothViewModel(midiManager) as T

}
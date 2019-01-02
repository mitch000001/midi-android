package org.mitchwork.midi.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel;
import org.mitchwork.midi.data.MidiDevice
import org.mitchwork.midi.data.MidiDeviceRepository

class MidiDeviceListViewModel internal constructor(
    midiDeviceRepository: MidiDeviceRepository
)  : ViewModel() {

    val devices: LiveData<List<MidiDevice>> = midiDeviceRepository.getDevices()
}

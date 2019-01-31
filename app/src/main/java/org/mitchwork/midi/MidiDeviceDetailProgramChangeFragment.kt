package org.mitchwork.midi

import android.content.Context
import android.media.midi.MidiManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import org.mitchwork.midi.data.AppDatabase
import org.mitchwork.midi.data.MidiDeviceRepository
import org.mitchwork.midi.databinding.FragmentMidiDeviceDetailProgramChangeBinding
import org.mitchwork.midi.viewmodels.MidiDeviceDetailViewModel
import org.mitchwork.midi.viewmodels.MidiDeviceDetailViewModelFactory

class MidiDeviceDetailProgramChangeFragment : Fragment() {

    private lateinit var viewModel: MidiDeviceDetailViewModel
    private lateinit var viewBinding: FragmentMidiDeviceDetailProgramChangeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentMidiDeviceDetailProgramChangeBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val deviceID = MidiDeviceDetailFragmentArgs.fromBundle(arguments!!).deviceID

        val midiManager: MidiManager = requireContext().getSystemService(Context.MIDI_SERVICE) as MidiManager
        val db = AppDatabase.getInstance(requireContext())
        val repository = MidiDeviceRepository.getInstance(db.midiDeviceDao(), db.controlChangeDao(), midiManager)
        val factory = MidiDeviceDetailViewModelFactory(repository, deviceID)

        viewModel = ViewModelProviders.of(this, factory).get(MidiDeviceDetailViewModel::class.java)

        viewBinding.executePendingBindings()
    }
}

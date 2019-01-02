package org.mitchwork.midi

import android.content.Context
import android.media.midi.MidiManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import org.mitchwork.midi.adapters.ControlChangeAdapter
import org.mitchwork.midi.data.AppDatabase
import org.mitchwork.midi.data.MidiDeviceRepository
import org.mitchwork.midi.databinding.FragmentMidiDeviceDetailControlChangesBinding
import org.mitchwork.midi.viewmodels.MidiDeviceDetailViewModel
import org.mitchwork.midi.viewmodels.MidiDeviceDetailViewModelFactory

class MidiDeviceDetailControlChangesFragment : Fragment() {

    private lateinit var viewModel: MidiDeviceDetailViewModel
    private lateinit var viewBinding: FragmentMidiDeviceDetailControlChangesBinding
    private lateinit var listAdapter: ControlChangeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentMidiDeviceDetailControlChangesBinding.inflate(inflater, container, false)
        listAdapter = ControlChangeAdapter()
        viewBinding.midiDeviceControlChanges.adapter = listAdapter

        val deviceID = MidiDeviceDetailFragmentArgs.fromBundle(arguments!!).deviceID

        val midiManager: MidiManager = requireContext().getSystemService(Context.MIDI_SERVICE) as MidiManager
        val db = AppDatabase.getInstance(requireContext())
        val repository = MidiDeviceRepository.getInstance(db.midiDeviceDao(), db.controlChangeDao(), midiManager)
        val factory = MidiDeviceDetailViewModelFactory(repository, deviceID)

        viewModel = ViewModelProviders.of(this, factory).get(MidiDeviceDetailViewModel::class.java)

        viewBinding.midiDeviceAddBindings.setOnClickListener {
            val direction = MidiDeviceDetailFragmentDirections.actionMidiDeviceDetailFragmentToContolChangeEditFragment(deviceID)
            it.findNavController().navigate(direction)
        }

        viewModel.controlChanges.observe(viewLifecycleOwner, Observer { controlChanges ->
            val hasControlChanges = (controlChanges != null && controlChanges.isNotEmpty())
            viewBinding.hasControlChanges = hasControlChanges
            if (hasControlChanges)
                listAdapter.submitList(controlChanges)
        })
        return viewBinding.root
    }

}

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
import org.mitchwork.midi.adapters.MidiDeviceListAdapter
import org.mitchwork.midi.data.AppDatabase
import org.mitchwork.midi.data.MidiDeviceRepository
import org.mitchwork.midi.databinding.FragmentMidiDeviceListBinding
import org.mitchwork.midi.viewmodels.MidiDeviceListViewModel
import org.mitchwork.midi.viewmodels.MidiDeviceListViewModelFactory

class MidiDeviceListFragment : Fragment() {

    private lateinit var viewModel: MidiDeviceListViewModel
    private lateinit var viewBinding: FragmentMidiDeviceListBinding
    private lateinit var listAdapter: MidiDeviceListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentMidiDeviceListBinding.inflate(inflater, container, false)
        viewBinding.setLifecycleOwner(this@MidiDeviceListFragment)

        return viewBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val midiManager: MidiManager = requireContext().getSystemService(Context.MIDI_SERVICE) as MidiManager
        val db = AppDatabase.getInstance(requireContext())
        val repository = MidiDeviceRepository.getInstance(db.midiDeviceDao(), db.controlChangeDao(), midiManager)
        val factory = MidiDeviceListViewModelFactory(repository)

        listAdapter = MidiDeviceListAdapter(object : MidiDeviceListAdapter.OnDeviceClickedListener{
            override fun onDeviceClicked(view: View, deviceID: String) {
                val direction = MidiDeviceListFragmentDirections.actionMidiDeviceListFragmentToMidiDeviceDetailFragment(deviceID)
                view.findNavController().navigate(direction)
            }
        })
        viewBinding.midiDeviceList.adapter = listAdapter

        viewModel = ViewModelProviders.of(this, factory).get(MidiDeviceListViewModel::class.java)
        viewBinding.fab.setOnClickListener {
            it.findNavController().navigate(R.id.action_midiDeviceListFragment_to_midiDeviceEditFragment)
        }

        viewModel.devices.observe(viewLifecycleOwner, Observer { devices ->
            viewBinding.hasDevices = (devices != null && devices.isNotEmpty())
            if (devices != null && devices.isNotEmpty())
                listAdapter.submitList(devices)
        })
        viewBinding.executePendingBindings()
    }
}

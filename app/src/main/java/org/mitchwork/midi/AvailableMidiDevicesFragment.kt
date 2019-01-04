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
import androidx.navigation.fragment.findNavController
import org.mitchwork.midi.adapters.AvailableMidiDeviceAdapter
import org.mitchwork.midi.data.MidiDeviceTracker
import org.mitchwork.midi.databinding.FragmentAvailableMidiDevicesBinding
import org.mitchwork.midi.viewmodels.AvailableMidiDevicesViewModel
import org.mitchwork.midi.viewmodels.AvailableMidiDevicesViewModelFactory

class AvailableMidiDevicesFragment : Fragment() {

    private lateinit var viewBinding: FragmentAvailableMidiDevicesBinding
    private lateinit var viewModel: AvailableMidiDevicesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentAvailableMidiDevicesBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val midiManager: MidiManager = requireContext().getSystemService(Context.MIDI_SERVICE) as MidiManager
        val factory = AvailableMidiDevicesViewModelFactory(midiManager)
        viewModel = ViewModelProviders.of(requireActivity(), factory).get(AvailableMidiDevicesViewModel::class.java)

        val adapter = AvailableMidiDeviceAdapter(object : AvailableMidiDeviceAdapter.OnDeviceClickedListener{
            override fun onDeviceClicked(view: View, device: MidiDeviceTracker) {
                viewModel.openMidiDevice(device)
                val direction = AvailableMidiDevicesFragmentDirections.actionAvailableMidiDevicesFragmentToMidiDeviceListFragment()
                findNavController().navigate(direction)
            }
        })

        viewBinding.midiDeviceList.adapter = adapter

        viewModel.availableDevices.observe(this, Observer {
            val hasDevices = it != null && it.isNotEmpty()
            viewBinding.hasDevices = hasDevices
            if (hasDevices) {
                adapter.submitList(it)
            }
        })

        viewBinding.clickListener = View.OnClickListener { viewModel.refreshDevices() }
    }

    override fun onResume() {
        super.onResume()

        if (arguments != null) {
            val bundle = AvailableMidiDevicesFragmentArgs.fromBundle(arguments!!)
            val scanResult = bundle.bluetoothResult

            if (scanResult != null) {
                viewModel.addBluetoothResult(scanResult)
            }
        }
    }
}

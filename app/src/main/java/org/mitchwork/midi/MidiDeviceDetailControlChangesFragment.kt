package org.mitchwork.midi

import android.content.Context
import android.media.midi.MidiInputPort
import android.media.midi.MidiManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import org.mitchwork.midi.adapters.ControlChangeAdapter
import org.mitchwork.midi.data.AppDatabase
import org.mitchwork.midi.data.ControlChangeWithMidiChannel
import org.mitchwork.midi.data.MidiDeviceRepository
import org.mitchwork.midi.databinding.FragmentMidiDeviceDetailControlChangesBinding
import org.mitchwork.midi.messages.ControlChangeMessage
import org.mitchwork.midi.viewmodels.AvailableMidiDevicesViewModel
import org.mitchwork.midi.viewmodels.AvailableMidiDevicesViewModelFactory
import org.mitchwork.midi.viewmodels.MidiDeviceDetailViewModel
import org.mitchwork.midi.viewmodels.MidiDeviceDetailViewModelFactory

class MidiDeviceDetailControlChangesFragment : Fragment() {

    private lateinit var viewModel: MidiDeviceDetailViewModel
    private lateinit var viewBinding: FragmentMidiDeviceDetailControlChangesBinding
    private lateinit var listAdapter: ControlChangeAdapter
    private lateinit var activityViewModel: AvailableMidiDevicesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentMidiDeviceDetailControlChangesBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val deviceID = MidiDeviceDetailFragmentArgs.fromBundle(arguments!!).deviceID

        val midiManager: MidiManager = requireContext().getSystemService(Context.MIDI_SERVICE) as MidiManager
        val db = AppDatabase.getInstance(requireContext())
        val repository = MidiDeviceRepository.getInstance(db.midiDeviceDao(), db.controlChangeDao(), midiManager)
        val factory = MidiDeviceDetailViewModelFactory(repository, deviceID)

        activityViewModel = ViewModelProviders.of(
            requireActivity(), AvailableMidiDevicesViewModelFactory(midiManager)
        ).get(AvailableMidiDevicesViewModel::class.java)

        viewModel = ViewModelProviders.of(this, factory).get(MidiDeviceDetailViewModel::class.java)

        listAdapter = ControlChangeAdapter(object : ControlChangeAdapter.OnControlChangeValueChangedListener {
            override fun onControlChangeValueChange(view: SeekBar, item: ControlChangeWithMidiChannel) {}

            override fun onControlChangeValueChanged(view: SeekBar, item: ControlChangeWithMidiChannel) {
                viewModel.updateControlChange(item.controlChange)
            }
        })
        viewBinding.midiDeviceControlChanges.adapter = listAdapter

        viewBinding.midiDeviceAddBindings.setOnClickListener {
            val direction =
                MidiDeviceDetailFragmentDirections.actionMidiDeviceDetailFragmentToContolChangeEditFragment(deviceID)
            it.findNavController().navigate(direction)
        }

        activityViewModel.midiDevice?.observe(viewLifecycleOwner, Observer { midiDevice ->
            Log.i("MIDI", "Midi device open: $midiDevice")
            if (midiDevice.info.inputPortCount != 0) {
                activityViewModel.openFirstInputPort()
            } else {
                Log.w("MIDI", "device has no input port: ${midiDevice.info.ports}")
            }
        })

        activityViewModel.midiInputPort?.observe(viewLifecycleOwner, MidiInputPortObserver(listAdapter))

        viewModel.controlChanges.observe(viewLifecycleOwner, Observer { controlChanges ->
            val hasControlChanges = (controlChanges != null && controlChanges.isNotEmpty())
            viewBinding.hasControlChanges = hasControlChanges
            if (hasControlChanges)
                listAdapter.submitList(controlChanges)
        })
    }

    class MidiInputPortObserver(private val listAdapter: ControlChangeAdapter): Observer<MidiInputPort> {
        private var listener: InputPortListener? = null

        override fun onChanged(inputPort: MidiInputPort?) {
            if (inputPort == null) {
                if (listener != null) listAdapter.removeOnControlChangeChangeValueListener(listener!!)
                return
            }
            if (listener != null && listener?.inputPort == inputPort) {
                return
            }
            listener = InputPortListener(inputPort)
            listAdapter.addOnControlChangeChangeValueListener(listener!!)
        }

        class InputPortListener(val inputPort: MidiInputPort) : ControlChangeAdapter.OnControlChangeValueChangedListener {
            override fun onControlChangeValueChanged(view: SeekBar, item: ControlChangeWithMidiChannel) {}

            override fun onControlChangeValueChange(view: SeekBar, item: ControlChangeWithMidiChannel) {
                val cc = ControlChangeMessage(
                    item.midiChannel,
                    item.controlChange.controllerNumber,
                    item.controlChange.value
                )
                val bytes = cc.toByteArray()
                inputPort.send(bytes, 0, bytes.size)
            }

        }
    }

}

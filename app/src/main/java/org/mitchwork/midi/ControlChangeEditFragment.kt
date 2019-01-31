package org.mitchwork.midi

import android.app.Activity
import android.content.Context
import android.media.midi.MidiManager
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import org.mitchwork.midi.data.AppDatabase
import org.mitchwork.midi.data.ControlChange
import org.mitchwork.midi.data.MidiDeviceRepository
import org.mitchwork.midi.databinding.FragmentControlChangeEditBinding
import org.mitchwork.midi.viewmodels.MidiDeviceDetailViewModel
import org.mitchwork.midi.viewmodels.MidiDeviceDetailViewModelFactory

class ControlChangeEditFragment : DialogFragment() {

    private lateinit var viewModel: MidiDeviceDetailViewModel
    private lateinit var viewBinding: FragmentControlChangeEditBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentControlChangeEditBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return viewBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val activity = requireActivity() as AppCompatActivity
        activity.supportActionBar?.apply {
            setTitle(R.string.create_cc_header)
            setHomeAsUpIndicator(R.drawable.ic_baseline_clear_24px)
        }

        val deviceID = MidiDeviceDetailFragmentArgs.fromBundle(arguments!!).deviceID

        val midiManager: MidiManager = requireContext().getSystemService(Context.MIDI_SERVICE) as MidiManager
        val db = AppDatabase.getInstance(requireContext())
        val repository = MidiDeviceRepository.getInstance(db.midiDeviceDao(), db.controlChangeDao(), midiManager)
        val factory = MidiDeviceDetailViewModelFactory(repository, deviceID)

        viewModel = ViewModelProviders.of(this, factory).get(MidiDeviceDetailViewModel::class.java)

        viewBinding.executePendingBindings()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.control_change_edit, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_save -> {
            val imm = requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view?.windowToken, 0)

            val name = viewBinding.name.text.toString()
            val controllerNumber = viewBinding.controllerNumber.text.toString().toInt()
            val controllerValue = viewBinding.controllerValue.text.toString().toInt()

            val cc = ControlChange(name, controllerNumber, controllerValue)
            viewModel.saveControlChange(cc)

            val deviceID = MidiDeviceDetailFragmentArgs.fromBundle(arguments!!).deviceID
            val direction = ControlChangeEditFragmentDirections.actionContolChangeEditFragmentToMidiDeviceDetailFragment(deviceID)

            Snackbar.make(view!!, "ControlChange created", Snackbar.LENGTH_SHORT).show()
            findNavController().navigate(direction)
            true
        }

        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }
}

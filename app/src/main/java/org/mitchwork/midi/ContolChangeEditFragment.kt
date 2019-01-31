package org.mitchwork.midi

import android.app.Activity
import android.content.Context
import android.media.midi.MidiManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import org.mitchwork.midi.data.AppDatabase
import org.mitchwork.midi.data.ControlChange
import org.mitchwork.midi.data.MidiDeviceRepository
import org.mitchwork.midi.databinding.FragmentControlChangeEditBinding
import org.mitchwork.midi.viewmodels.MidiDeviceDetailViewModel
import org.mitchwork.midi.viewmodels.MidiDeviceDetailViewModelFactory

class ContolChangeEditFragment : Fragment() {

    private lateinit var viewModel: MidiDeviceDetailViewModel
    private lateinit var viewBinding: FragmentControlChangeEditBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentControlChangeEditBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val activity = requireActivity() as AppCompatActivity
        activity.supportActionBar?.setTitle(R.string.create_cc_header)

        val deviceID = MidiDeviceDetailFragmentArgs.fromBundle(arguments!!).deviceID

        val midiManager: MidiManager = requireContext().getSystemService(Context.MIDI_SERVICE) as MidiManager
        val db = AppDatabase.getInstance(requireContext())
        val repository = MidiDeviceRepository.getInstance(db.midiDeviceDao(), db.controlChangeDao(), midiManager)
        val factory = MidiDeviceDetailViewModelFactory(repository, deviceID)

        viewModel = ViewModelProviders.of(this, factory).get(MidiDeviceDetailViewModel::class.java)

        val direction = ContolChangeEditFragmentDirections.actionContolChangeEditFragmentToMidiDeviceDetailFragment(deviceID)

        viewBinding.cancelClickListener = View.OnClickListener {
            it.findNavController().popBackStack()
        }

        viewBinding.saveClickListener = View.OnClickListener {
            val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view?.windowToken, 0)

            val name = viewBinding.name.text.toString()
            val controllerNumber = viewBinding.controllerNumber.text.toString().toInt()
            val controllerValue = viewBinding.controllerValue.text.toString().toInt()

            val cc = ControlChange(name, controllerNumber, controllerValue)

            viewModel.saveControlChange(cc)

            Snackbar.make(view!!, "ControlChange created", Snackbar.LENGTH_SHORT).show()
            it.findNavController().navigate(direction)
        }
        viewBinding.executePendingBindings()
    }

}

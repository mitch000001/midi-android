package org.mitchwork.midi


import android.app.Activity
import android.content.Context
import android.media.midi.MidiManager
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import org.mitchwork.midi.data.AppDatabase
import org.mitchwork.midi.data.MidiDeviceRepository
import org.mitchwork.midi.databinding.FragmentMidiDeviceEditBinding
import org.mitchwork.midi.viewmodels.MidiDeviceEditViewModel
import org.mitchwork.midi.viewmodels.MidiDeviceEditViewModelFactory
import org.mitchwork.midi.views.BlankValidator
import org.mitchwork.midi.views.Form


/**
 * A simple [Fragment] subclass.
 *
 */
class MidiDeviceEditFragment : DialogFragment() {

    private lateinit var viewModel: MidiDeviceEditViewModel
    private lateinit var viewBinding: FragmentMidiDeviceEditBinding
    private lateinit var form: Form

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentMidiDeviceEditBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return viewBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val activity = requireActivity() as AppCompatActivity
        activity.supportActionBar?.apply {
            setTitle(R.string.create_mididevice_header)
            setHomeAsUpIndicator(R.drawable.ic_baseline_clear_24px)
        }
        val midiManager: MidiManager = requireContext().getSystemService(Context.MIDI_SERVICE) as MidiManager

        val db = AppDatabase.getInstance(requireContext())
        val repository = MidiDeviceRepository.getInstance(db.midiDeviceDao(), db.controlChangeDao(), midiManager)

        viewModel = ViewModelProviders.of(this, MidiDeviceEditViewModelFactory(repository))
            .get(MidiDeviceEditViewModel::class.java)
        viewBinding.device = viewModel

        form = Form()
        form.addValidator(BlankValidator(activity, viewBinding.midiDeviceEditName))
        form.addValidator(BlankValidator(activity, viewBinding.midiDeviceEditMidiChannel))
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.midi_device_edit, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_save -> {
            if (form.validate()) {
                val imm = requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view?.windowToken, 0)

                viewModel.name.value = viewBinding.midiDeviceEditName.text.toString()
                viewModel.midiChannel.value = viewBinding.midiDeviceEditMidiChannel.text.toString().toInt()
                viewModel.save()

                Snackbar.make(view!!, "created new device", Snackbar.LENGTH_LONG).show()
                val direction = MidiDeviceEditFragmentDirections.actionMidiDeviceEditFragmentToMidiDeviceListFragment()
                findNavController().navigate(direction)
                true
            } else {
                false
            }

        }

        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }
}

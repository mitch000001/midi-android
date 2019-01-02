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
import org.mitchwork.midi.data.MidiDeviceRepository
import org.mitchwork.midi.databinding.FragmentMidiDeviceEditBinding
import org.mitchwork.midi.viewmodels.MidiDeviceEditViewModel
import org.mitchwork.midi.viewmodels.MidiDeviceEditViewModelFactory



/**
 * A simple [Fragment] subclass.
 *
 */
class MidiDeviceEditFragment : Fragment() {

    private lateinit var viewModel: MidiDeviceEditViewModel
    private lateinit var viewBinding: FragmentMidiDeviceEditBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentMidiDeviceEditBinding.inflate(inflater, container, false)

        viewBinding.setLifecycleOwner(this@MidiDeviceEditFragment)

        return viewBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val activity = requireActivity() as AppCompatActivity
        activity.supportActionBar?.setTitle(R.string.create_mididevice_header)

        val midiManager: MidiManager = requireContext().getSystemService(Context.MIDI_SERVICE) as MidiManager

        val db = AppDatabase.getInstance(requireContext())
        val repository = MidiDeviceRepository.getInstance(db.midiDeviceDao(), db.controlChangeDao(), midiManager)

        viewModel = ViewModelProviders.of(this, MidiDeviceEditViewModelFactory(repository)).get(MidiDeviceEditViewModel::class.java)
        viewBinding.device = viewModel
        viewBinding.cancelClickListener = View.OnClickListener {
            it.findNavController().popBackStack()
        }
        viewBinding.saveClickListener = View.OnClickListener {
            val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view?.windowToken, 0)

            viewModel.name.value = viewBinding.midiDeviceEditName.text.toString()
            viewModel.midiChannel.value = viewBinding.midiDeviceEditMidiChannel.text.toString().toInt()
            viewModel.save()

            Snackbar.make(view!!, "created new device", Snackbar.LENGTH_LONG).show()
            val direction = MidiDeviceEditFragmentDirections.actionMidiDeviceEditFragmentToMidiDeviceListFragment()
            it.findNavController().navigate(direction)
        }
    }

}

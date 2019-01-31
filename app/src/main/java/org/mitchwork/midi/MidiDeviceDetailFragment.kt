package org.mitchwork.midi

import android.content.Context
import android.media.midi.MidiManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE
import androidx.fragment.app.transaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.mitchwork.midi.data.AppDatabase
import org.mitchwork.midi.data.MidiDeviceRepository
import org.mitchwork.midi.databinding.FragmentMidiDeviceDetailBinding
import org.mitchwork.midi.viewmodels.MidiDeviceDetailViewModel
import org.mitchwork.midi.viewmodels.MidiDeviceDetailViewModelFactory

class MidiDeviceDetailFragment : Fragment() {

    private lateinit var viewModel: MidiDeviceDetailViewModel
    private lateinit var viewBinding: FragmentMidiDeviceDetailBinding
    private lateinit var bottomNavigation: BottomNavigationView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentMidiDeviceDetailBinding.inflate(inflater, container, false)
        viewBinding.setLifecycleOwner(this)
        bottomNavigation = viewBinding.bottomNavigation

        return viewBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val activity = requireActivity() as AppCompatActivity
        val fragmentManager = activity.supportFragmentManager

        val fragmentArgs = MidiDeviceDetailFragmentArgs.fromBundle(arguments!!)
        val deviceID = fragmentArgs.deviceID

        val midiManager: MidiManager = requireContext().getSystemService(Context.MIDI_SERVICE) as MidiManager

        val db = AppDatabase.getInstance(requireContext())
        val repository = MidiDeviceRepository.getInstance(db.midiDeviceDao(), db.controlChangeDao(), midiManager)
        val factory = MidiDeviceDetailViewModelFactory(repository, deviceID)

        bottomNavigation.setOnNavigationItemSelectedListener { menuItem: MenuItem ->
            menuItem.isChecked = true

            when (menuItem.itemId) {
                R.id.action_control_change -> {
                    val fragment = MidiDeviceDetailControlChangesFragment()
                    fragment.arguments = arguments
                    fragmentManager.transaction {
                        replace(R.id.outer_content, fragment)
                        setTransition(TRANSIT_FRAGMENT_FADE)
                    }
                    true
                }
                R.id.action_program_change -> {
                    val fragment = MidiDeviceDetailProgramChangeFragment()
                    fragment.arguments = arguments
                    fragmentManager.transaction {
                        replace(R.id.outer_content, fragment)
                        setTransition(TRANSIT_FRAGMENT_FADE)
                    }
                    true
                }
                R.id.action_sysex -> {
                    val fragment = MidiDeviceDetailSysExFragment()
                    fragment.arguments = arguments
                    fragmentManager.transaction {
                        replace(R.id.outer_content, fragment)
                        setTransition(TRANSIT_FRAGMENT_FADE)
                    }
                    true
                }
                else -> {
                    Log.e("MIDI", "selected menu item: ${menuItem.itemId}")
                    false
                }
            }
        }

        bottomNavigation.selectedItemId = R.id.action_control_change

        viewModel = ViewModelProviders.of(this, factory).get(MidiDeviceDetailViewModel::class.java)

        viewModel.device.observe(viewLifecycleOwner, Observer { device ->
            activity.supportActionBar?.title = device.name
        })
    }

}

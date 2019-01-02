package org.mitchwork.midi

import android.content.Context
import android.media.midi.MidiManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.tabs.TabLayout
import org.mitchwork.midi.adapters.MidiDeviceDetailTabAdapter
import org.mitchwork.midi.data.AppDatabase
import org.mitchwork.midi.data.MidiDeviceRepository
import org.mitchwork.midi.databinding.FragmentMidiDeviceDetailBinding
import org.mitchwork.midi.viewmodels.MidiDeviceDetailViewModel
import org.mitchwork.midi.viewmodels.MidiDeviceDetailViewModelFactory

class MidiDeviceDetailFragment : Fragment() {

    private lateinit var viewModel: MidiDeviceDetailViewModel
    private lateinit var viewBinding: FragmentMidiDeviceDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentMidiDeviceDetailBinding.inflate(inflater, container, false)
        viewBinding.setLifecycleOwner(this)

        return viewBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val activity = requireActivity() as AppCompatActivity

        val deviceID = MidiDeviceDetailFragmentArgs.fromBundle(arguments!!).deviceID

        val midiManager: MidiManager = requireContext().getSystemService(Context.MIDI_SERVICE) as MidiManager

        val db = AppDatabase.getInstance(requireContext())
        val repository = MidiDeviceRepository.getInstance(db.midiDeviceDao(), db.controlChangeDao(), midiManager)
        val factory = MidiDeviceDetailViewModelFactory(repository, deviceID)

        val adapter = MidiDeviceDetailTabAdapter(activity.supportFragmentManager, deviceID)

        viewBinding.viewPager.adapter = adapter
        viewBinding.tabs.setupWithViewPager(viewBinding.viewPager)
        viewBinding.viewPager.addOnPageChangeListener(TabChangeListener(viewBinding.tabs))
        viewBinding.viewPager.currentItem = viewBinding.viewPager.currentItem

        viewModel = ViewModelProviders.of(this, factory).get(MidiDeviceDetailViewModel::class.java)

        viewModel.device.observe(viewLifecycleOwner, Observer { device ->
            activity.supportActionBar?.title = device.name
        })
    }

    class TabChangeListener(private val tabLayout: TabLayout) : TabLayout.TabLayoutOnPageChangeListener(tabLayout) {
        data class Tab(val iconResourceID: Int, val textResourceID: Int)

        private val tabs: List<Tab> = listOf(
            Tab(R.drawable.ic_control_change, R.string.midi_device_details_tab_control_changes),
            Tab(R.drawable.ic_sysex, R.string.midi_device_details_tab_sysex)
        )

        init {
            for ((index, tab) in tabs.withIndex()) {
                val tabItem = tabLayout.getTabAt(index)
                tabItem?.setIcon(tab.iconResourceID)
                tabItem?.setText(tab.textResourceID)
            }
        }

        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            val tabItem = tabLayout.getTabAt(position)
            val tab = tabs[position]
            tabItem?.setIcon(tab.iconResourceID)
            tabItem?.setText(tab.textResourceID)
        }
    }

}

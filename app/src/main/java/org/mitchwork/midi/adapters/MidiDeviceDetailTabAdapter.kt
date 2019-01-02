package org.mitchwork.midi.adapters

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import org.mitchwork.midi.MidiDeviceDetailControlChangesFragment
import org.mitchwork.midi.MidiDeviceDetailSysExFragment

class MidiDeviceDetailTabAdapter(
    fm: FragmentManager,
    deviceID: String
) : FragmentStatePagerAdapter(fm) {
    private val arguments: Bundle = bundleOf("deviceID" to deviceID)

    override fun getItem(position: Int): Fragment =
        when (position) {
            0 -> {
                val fragment = MidiDeviceDetailControlChangesFragment()
                fragment.arguments = arguments
                fragment
            }
            1 -> {
                val fragment = MidiDeviceDetailSysExFragment()
                fragment.arguments = arguments
                fragment
            }
            else -> {
                throw IndexOutOfBoundsException()
            }
        }

    override fun getCount(): Int = 2

    override fun getPageTitle(position: Int): CharSequence? {
        return null
    }
}
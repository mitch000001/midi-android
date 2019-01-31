package org.mitchwork.midi.adapters

import androidx.recyclerview.widget.DiffUtil
import org.mitchwork.midi.data.MidiDeviceTracker

class MidiDeviceTrackerDiffCallback : DiffUtil.ItemCallback<MidiDeviceTracker>() {
    override fun areItemsTheSame(oldItem: MidiDeviceTracker, newItem: MidiDeviceTracker): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: MidiDeviceTracker, newItem: MidiDeviceTracker): Boolean {
        return oldItem == newItem
    }
}
package org.mitchwork.midi.adapters

import androidx.recyclerview.widget.DiffUtil
import org.mitchwork.midi.data.MidiDevice

class MidiDeviceDiffCallback : DiffUtil.ItemCallback<MidiDevice>() {

    override fun areItemsTheSame(oldItem: MidiDevice, newItem: MidiDevice): Boolean {
        return oldItem.uid == newItem.uid
    }

    override fun areContentsTheSame(oldItem: MidiDevice, newItem: MidiDevice): Boolean {
        return oldItem.equals(newItem)
    }

}
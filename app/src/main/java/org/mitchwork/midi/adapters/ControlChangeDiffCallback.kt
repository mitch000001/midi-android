package org.mitchwork.midi.adapters

import androidx.recyclerview.widget.DiffUtil
import org.mitchwork.midi.data.ControlChangeWithMidiChannel

class ControlChangeDiffCallback : DiffUtil.ItemCallback<ControlChangeWithMidiChannel>() {

    override fun areItemsTheSame(oldItem: ControlChangeWithMidiChannel, newItem: ControlChangeWithMidiChannel): Boolean {
        return oldItem.controlChange.uid == newItem.controlChange.uid
    }

    override fun areContentsTheSame(oldItem: ControlChangeWithMidiChannel, newItem: ControlChangeWithMidiChannel): Boolean {
        return oldItem.controlChange == newItem.controlChange
    }

}
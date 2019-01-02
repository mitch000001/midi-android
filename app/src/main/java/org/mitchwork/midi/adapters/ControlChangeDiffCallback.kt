package org.mitchwork.midi.adapters

import androidx.recyclerview.widget.DiffUtil
import org.mitchwork.midi.data.ControlChange

class ControlChangeDiffCallback : DiffUtil.ItemCallback<ControlChange>() {

    override fun areItemsTheSame(oldItem: ControlChange, newItem: ControlChange): Boolean {
        return oldItem.uid == newItem.uid
    }

    override fun areContentsTheSame(oldItem: ControlChange, newItem: ControlChange): Boolean {
        return oldItem.equals(newItem)
    }

}
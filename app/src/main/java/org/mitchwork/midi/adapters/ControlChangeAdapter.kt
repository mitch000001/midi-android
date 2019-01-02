package org.mitchwork.midi.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.mitchwork.midi.data.ControlChange
import org.mitchwork.midi.databinding.ControlChangeItemBinding

class ControlChangeAdapter: ListAdapter<ControlChange, ControlChangeAdapter.ViewHolder>(ControlChangeDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ControlChangeItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val change = getItem(position)
        holder.apply {
            bind(change)
            itemView.tag = change
        }
    }

    class ViewHolder(
        val binding: ControlChangeItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ControlChange) {
            with(binding) {
                controlChange = item
                executePendingBindings()
            }
        }
    }
}
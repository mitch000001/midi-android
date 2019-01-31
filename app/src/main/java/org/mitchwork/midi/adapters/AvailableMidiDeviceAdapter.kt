package org.mitchwork.midi.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.mitchwork.midi.data.MidiDeviceTracker
import org.mitchwork.midi.databinding.AvailableMidiDeviceItemBinding

class AvailableMidiDeviceAdapter(
    private val onDeviceClickedListener: OnDeviceClickedListener
) : ListAdapter<MidiDeviceTracker, AvailableMidiDeviceAdapter.ViewHolder>(MidiDeviceTrackerDiffCallback()) {
    interface OnDeviceClickedListener {
        fun onDeviceClicked(view: View, device: MidiDeviceTracker)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            AvailableMidiDeviceItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.apply {
            bind(createItemSelectClickListener(item), AvailableMidiDevicePresenter(item))
            itemView.tag = item
        }
    }

    private fun createItemSelectClickListener(device: MidiDeviceTracker): View.OnClickListener = View.OnClickListener {
        onDeviceClickedListener.onDeviceClicked(it, device)
    }

    class ViewHolder(private val binding: AvailableMidiDeviceItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(clickListener: View.OnClickListener, device: AvailableMidiDevicePresenter) {
            with(binding) {
                this.device = device
                this.clickListener = clickListener
                executePendingBindings()
            }
        }
    }
}

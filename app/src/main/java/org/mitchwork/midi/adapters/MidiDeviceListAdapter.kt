package org.mitchwork.midi.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.mitchwork.midi.data.MidiDevice
import org.mitchwork.midi.databinding.MidiDeviceItemBinding

class MidiDeviceListAdapter(
    private val onDeviceClickedListener: OnDeviceClickedListener
): ListAdapter<MidiDevice, MidiDeviceListAdapter.ViewHolder>(MidiDeviceDiffCallback()) {

    interface OnDeviceClickedListener {
        fun onDeviceClicked(view: View, deviceID: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            MidiDeviceItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val device = getItem(position)
        holder.apply {
            bind(createOnClickListener(device.uid), device)
            itemView.tag = device
        }
    }

    private fun createOnClickListener(deviceID: String): View.OnClickListener {
        return View.OnClickListener {
            onDeviceClickedListener.onDeviceClicked(it, deviceID)
        }
    }

    class ViewHolder(
        val binding: MidiDeviceItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(listener: View.OnClickListener, item: MidiDevice) {
            with(binding) {
                clickListener = listener
                device = item
                executePendingBindings()
            }
        }
    }
}
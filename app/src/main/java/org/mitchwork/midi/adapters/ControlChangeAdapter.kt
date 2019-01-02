package org.mitchwork.midi.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.mitchwork.midi.data.ControlChange
import org.mitchwork.midi.databinding.ControlChangeItemBinding
import org.mitchwork.midi.viewmodels.MidiDeviceDetailViewModel

class ControlChangeAdapter(
    private val viewModel: MidiDeviceDetailViewModel
): ListAdapter<ControlChange, ControlChangeAdapter.ViewHolder>(ControlChangeDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ControlChangeItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false),
            viewModel
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val change = getItem(position)
        holder.apply {
            bind(change)
            itemView.tag = change
        }
    }

    class ViewHolder(
        val binding: ControlChangeItemBinding,
        private val viewModel: MidiDeviceDetailViewModel
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ControlChange) {
            with(binding) {
                controlChange = item
                binding.valueSeekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                    override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                    override fun onStopTrackingTouch(seekBar: SeekBar?) {
                        if (seekBar == null) return
                        item.value = seekBar.progress
                        viewModel.updateControlChange(item)
                    }

                    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {}
                })
                executePendingBindings()
            }
        }
    }
}
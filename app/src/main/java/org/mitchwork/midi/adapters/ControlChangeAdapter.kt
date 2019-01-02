package org.mitchwork.midi.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.mitchwork.midi.data.ControlChange
import org.mitchwork.midi.databinding.ControlChangeItemBinding

class ControlChangeAdapter(
    private val onControlChangeValueChangedListener: OnControlChangeValueChangedListener
): ListAdapter<ControlChange, ControlChangeAdapter.ViewHolder>(ControlChangeDiffCallback()) {
    interface OnControlChangeValueChangedListener {
        fun onControlChangeValueChanged(view: View, item: ControlChange)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ControlChangeItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val change = getItem(position)
        holder.apply {
            bind(onControlChangeValueChangedListener, change)
            itemView.tag = change
        }
    }

    class ViewHolder(
        val binding: ControlChangeItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(onChangedListener: OnControlChangeValueChangedListener, item: ControlChange) {
            with(binding) {
                controlChange = item
                binding.valueSeekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                    override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                    override fun onStopTrackingTouch(seekBar: SeekBar?) {
                        if (seekBar == null) return
                        item.value = seekBar.progress
                        onChangedListener.onControlChangeValueChanged(seekBar, item)
                    }

                    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {}
                })
                executePendingBindings()
            }
        }
    }
}
package org.mitchwork.midi.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.mitchwork.midi.data.ControlChangeWithMidiChannel
import org.mitchwork.midi.databinding.ControlChangeItemBinding

class ControlChangeAdapter(
    private val onControlChangeValueChangedListener: OnControlChangeValueChangedListener
): ListAdapter<ControlChangeWithMidiChannel, ControlChangeAdapter.ViewHolder>(ControlChangeDiffCallback()) {
    interface OnControlChangeValueChangedListener {
        fun onControlChangeValueChanged(view: SeekBar, item: ControlChangeWithMidiChannel)
        fun onControlChangeValueChange(view: SeekBar, item: ControlChangeWithMidiChannel)
    }

    private var onControlChangeValueChangedListeners: List<OnControlChangeValueChangedListener>
    init {
        onControlChangeValueChangedListeners = arrayListOf(onControlChangeValueChangedListener)
    }

    fun addOnControlChangeChangeValueListener(listener: OnControlChangeValueChangedListener) {
        onControlChangeValueChangedListeners += listener
    }

    fun removeOnControlChangeChangeValueListener(listener: OnControlChangeValueChangedListener) {
        onControlChangeValueChangedListeners = onControlChangeValueChangedListeners.filterNot { it == listener }
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
            bind(onControlChangeValueChangedListeners, change)
            itemView.tag = change
        }
    }

    class ViewHolder(
        val binding: ControlChangeItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(onChangeListeners: List<OnControlChangeValueChangedListener>, item: ControlChangeWithMidiChannel) {
            with(binding) {
                controlChange = item.controlChange
                binding.valueSeekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                    override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                    override fun onStopTrackingTouch(seekBar: SeekBar?) {
                        if (seekBar == null) return
                        for (listener in onChangeListeners) {
                            listener.onControlChangeValueChanged(seekBar, item)
                        }
                    }

                    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                        if (seekBar == null) return
                        item.controlChange.value = progress
                        for (listener in onChangeListeners) {
                            listener.onControlChangeValueChange(seekBar, item)
                        }
                    }
                })
                executePendingBindings()
            }
        }
    }
}
package org.mitchwork.midi.adapters

import android.bluetooth.le.ScanResult
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.mitchwork.midi.databinding.BluetoothDeviceItemBinding

class BluetoothDeviceAdapter(
    private val selectedListener: OnBluetoothSelectedListener
) : ListAdapter<ScanResult, BluetoothDeviceAdapter.ViewHolder>(BluetoothScanResultDiffCallback()) {
    interface OnBluetoothSelectedListener {
        fun onBluetoothResultSelected(result: ScanResult)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return BluetoothDeviceAdapter.ViewHolder(
            BluetoothDeviceItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val result = getItem(position)
        holder.apply {
            bind(createConnectClickListener(result), result)
            itemView.tag = result
        }
    }

    private fun createConnectClickListener(result: ScanResult): View.OnClickListener = View.OnClickListener {
        selectedListener.onBluetoothResultSelected(result)
    }

    class ViewHolder(private val binding: BluetoothDeviceItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(itemClickListener: View.OnClickListener, item: ScanResult) {
            with(binding) {
                result = item
                clickListener = itemClickListener
                executePendingBindings()
            }
        }
    }

}

class BluetoothScanResultDiffCallback : DiffUtil.ItemCallback<ScanResult>() {
    override fun areItemsTheSame(oldItem: ScanResult, newItem: ScanResult): Boolean = oldItem.timestampNanos == newItem.timestampNanos

    override fun areContentsTheSame(oldItem: ScanResult, newItem: ScanResult): Boolean {
        return oldItem.device.address == newItem.device.address
    }
}
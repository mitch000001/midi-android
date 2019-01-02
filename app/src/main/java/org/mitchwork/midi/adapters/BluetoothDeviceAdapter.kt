package org.mitchwork.midi.adapters

import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanResult
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.mitchwork.midi.databinding.BluetoothDeviceItemBinding

class BluetoothDeviceAdapter : ListAdapter<ScanResult, BluetoothDeviceAdapter.ViewHolder>(BluetoothScanResultDiffCallback()) {

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
            bind(createConnectClickListener(result.device), result)
            itemView.tag = result
        }
    }

    fun createConnectClickListener(device: BluetoothDevice): View.OnClickListener = View.OnClickListener {

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
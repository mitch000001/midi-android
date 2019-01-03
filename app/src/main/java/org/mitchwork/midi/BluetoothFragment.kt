package org.mitchwork.midi

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.ParcelUuid
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.android.material.snackbar.Snackbar
import org.mitchwork.midi.adapters.BluetoothDeviceAdapter
import org.mitchwork.midi.databinding.FragmentBluetoothBinding
import java.util.*

private val BluetoothAdapter.isDisabled: Boolean
    get() = !isEnabled

class BluetoothFragment : Fragment() {

    private lateinit var handler: Handler
    private lateinit var viewBinding: FragmentBluetoothBinding
    private lateinit var bluetoothAdapter: BluetoothAdapter
    private lateinit var listAdapter: BluetoothDeviceAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentBluetoothBinding.inflate(inflater, container, false)
        viewBinding.bluetoothList.addItemDecoration(
            DividerItemDecoration(
                viewBinding.bluetoothList.context,
                DividerItemDecoration.VERTICAL
            )
        )
        return viewBinding.root
    }

    private val filters = mutableListOf<ScanFilter>(
        ScanFilter.Builder().setServiceUuid(
            ParcelUuid(UUID.fromString(MIDI_BLE_UUID))
        ).build()
    )

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        handler = Handler()

        val activity = requireActivity() as AppCompatActivity
        activity.supportActionBar?.title = getString(R.string.bluetooth_devices)

        val bluetoothManager = requireContext().getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter

        listAdapter = BluetoothDeviceAdapter(object : BluetoothDeviceAdapter.OnBluetoothSelectedListener{
            override fun onBluetoothResultSelected(result: ScanResult) {
                val direction = BluetoothFragmentDirections.actionSelectBluetoothDevice(result)
                findNavController().navigate(direction)
            }
        })
        viewBinding.bluetoothList.adapter = listAdapter

        viewBinding.clickListener = View.OnClickListener {
            if (bluetoothAdapter.isDisabled) {
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
            } else {
                startScanningIfPermitted()
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            scanForDevices(listAdapter)
        } else {
            Toast.makeText(activity, R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // User chose not to enable Bluetooth.
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(activity, R.string.bluetooth_is_required, Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
            return
        }
        scanForDevices(listAdapter)
    }

    private fun startScanningIfPermitted() {
        if (activity == null || activity!!.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                Toast.makeText(activity, R.string.why_btle_location, Toast.LENGTH_LONG).show()
            }
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION
            )
        } else {
            scanForDevices(listAdapter)
        }
    }

    private fun scanForDevices(adapter: BluetoothDeviceAdapter) {
        val scanCallback = object : ScanCallback() {
            override fun onScanFailed(errorCode: Int) {
                super.onScanFailed(errorCode)
                Snackbar.make(view!!, "bluetooth scan failed", Snackbar.LENGTH_LONG).show()
            }

            override fun onScanResult(callbackType: Int, result: ScanResult?) {
                super.onScanResult(callbackType, result)
                if (result != null) {
                    adapter.submitList(arrayListOf(result))
                }
            }

            override fun onBatchScanResults(results: MutableList<ScanResult>?) {
                super.onBatchScanResults(results)
                if (results != null) {
                    adapter.submitList(results)
                }
            }
        }
        val scanner = bluetoothAdapter.bluetoothLeScanner
        handler.postDelayed({
            scanner?.stopScan(scanCallback)
        }, SCAN_PERIOD)
        scanner.startScan(filters, null, scanCallback)
    }

    companion object {
        private const val REQUEST_ENABLE_BT: Int = 1
        private const val PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 33
        private const val SCAN_PERIOD: Long = 10000
        private const val MIDI_BLE_UUID = "03B80E5A-EDE8-4B33-A751-6CE34EC4C700"
    }
}


package org.mitchwork.midi.adapters

import android.media.midi.MidiDeviceInfo
import org.mitchwork.midi.data.MidiDeviceTracker

class AvailableMidiDevicePresenter(private val device: MidiDeviceTracker) {
    val name = device.midiDevice?.properties?.getString(MidiDeviceInfo.PROPERTY_NAME)
        ?: device.bluetoothDevice?.device?.name ?: ""
    val productName = device.midiDevice?.properties?.getString(MidiDeviceInfo.PROPERTY_PRODUCT)
        ?: device.bluetoothDevice?.device?.address ?: ""
    val manufacturer = device.midiDevice?.properties?.getString(MidiDeviceInfo.PROPERTY_MANUFACTURER)
        ?: ""
    val inputPorts: String
        get() {
            if (device.midiDevice != null) {
                if (device.midiDevice.inputPortCount != 0) {
                    return "Input ports:\n" + (device.midiDevice?.ports
                        ?.filter { it.type == MidiDeviceInfo.PortInfo.TYPE_INPUT }
                        ?.map { it -> run {
                            if (it.name != "") {
                                return it.name + " " + it.portNumber
                            }
                            return it.portNumber.toString()
                        }}
                        ?.joinToString("\n") ?: " - ")
                }
            }
            return "Input ports: -"
        }
    val outputPorts: String
        get() {
            if (device.midiDevice != null) {
                if (device.midiDevice.outputPortCount != 0) {
                    return "Output ports:\n" + (device.midiDevice?.ports
                        ?.filter { it.type == MidiDeviceInfo.PortInfo.TYPE_OUTPUT }
                        ?.map { it -> run {
                            if (it.name != "") {
                                return it.name + " " + it.portNumber
                            }
                            return it.portNumber.toString()
                        }}
                        ?.joinToString("\n") ?: " - ")
                }
            }
            return "Output ports: -"
        }
}
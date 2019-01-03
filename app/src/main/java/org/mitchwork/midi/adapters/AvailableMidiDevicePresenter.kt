package org.mitchwork.midi.adapters

import android.media.midi.MidiDeviceInfo
import org.mitchwork.midi.data.MidiDeviceTracker

class AvailableMidiDevicePresenter(device: MidiDeviceTracker) {
    val name = device.midiDevice?.properties?.getString(MidiDeviceInfo.PROPERTY_NAME)
        ?: device.bluetoothDevice?.device?.name ?: ""
    val productName = device.midiDevice?.properties?.getString(MidiDeviceInfo.PROPERTY_PRODUCT)
        ?: device.bluetoothDevice?.device?.address ?: ""
    val manufacturer = device.midiDevice?.properties?.getString(MidiDeviceInfo.PROPERTY_MANUFACTURER)
        ?: ""
    val inputPorts = "Input ports:\n" + (device.midiDevice?.ports
        ?.filter { it.type == MidiDeviceInfo.PortInfo.TYPE_INPUT }
        ?.map { it.name + " " + it.portNumber }
        ?.reduce { acc, s ->  acc + "\n" + s }
        ?: " - ")
    val outputPorts = "Output ports:\n" + (device.midiDevice?.ports
        ?.filter { it.type == MidiDeviceInfo.PortInfo.TYPE_OUTPUT }
        ?.map { it.name + " " + it.portNumber }
        ?.reduce { acc, s ->  acc + "\n" + s } ?: " - ")

}
package org.mitchwork.midi.messages

import android.media.midi.MidiReceiver
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList

interface ControlChangeListener {
    fun onControlChangeMessage(cc: ControlChangeMessage)
}

class MidiMessageParser() : MidiReceiver() {

    private var input: ByteArray = ByteArray(0)
    private val controlChangeMessages: ObservableList<ControlChangeMessage> = ObservableArrayList()
    private val controlChangeMessageListener: List<ControlChangeListener> = ArrayList()

    init {
        controlChangeMessages.addOnListChangedCallback(ControlChangeMessageChangedCallback(controlChangeMessageListener))
    }

    override fun onSend(msg: ByteArray?, offset: Int, count: Int, timestamp: Long) {
        if (msg == null) return
        val message = msg.sliceArray(offset until offset+count)
        input += message
        parse()
    }

    private fun parse() {
        while (hasNext()) {
            val n = next()
            val status = MidiStatusBytes.fromByte(n)
            when (status) {
                MidiStatusBytes.UNKNOWN -> {}
                MidiStatusBytes.CONTROL_CHANGE -> {
                    parseControlChange(n)
                }
                else -> throw IllegalStateException()
            }
        }
    }

    private fun parseControlChange(byte: Byte) {
        val midiChannel = byte.minus(MidiStatusBytes.CONTROL_CHANGE.value) + 1
        val controllerNumber = next()
        val controllerValue = next()
        val cc =  ControlChangeMessage(midiChannel, controllerNumber.toInt(), controllerValue.toInt())
        controlChangeMessages.add(cc)
    }

    private fun hasNext(): Boolean = input.isNotEmpty()

    private fun next(): Byte {
        val b = input[0]
        input = input.drop(1).toByteArray()
        return b
    }

}

class ControlChangeMessageChangedCallback(
    private val listenerList: List<ControlChangeListener>
) : ObservableList.OnListChangedCallback<ObservableList<ControlChangeMessage>>() {
    override fun onChanged(sender: ObservableList<ControlChangeMessage>?) {}

    override fun onItemRangeRemoved(
        sender: ObservableList<ControlChangeMessage>?,
        positionStart: Int,
        itemCount: Int
    ) {}

    override fun onItemRangeMoved(
        sender: ObservableList<ControlChangeMessage>?,
        fromPosition: Int,
        toPosition: Int,
        itemCount: Int
    ) {}

    override fun onItemRangeInserted(
        sender: ObservableList<ControlChangeMessage>?,
        positionStart: Int,
        itemCount: Int
    ) {
        for (listener in listenerList) {
            for (i in positionStart until positionStart+itemCount) {
                val message = sender?.get(i)
                if (message != null) {
                    listener.onControlChangeMessage(message)
                }
            }
        }
    }

    override fun onItemRangeChanged(
        sender: ObservableList<ControlChangeMessage>?,
        positionStart: Int,
        itemCount: Int
    ) {}
}
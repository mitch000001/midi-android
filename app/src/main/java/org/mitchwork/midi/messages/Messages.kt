package org.mitchwork.midi.messages

interface BinaryMarshaller {
    fun toByteArray(): ByteArray
}

data class NoteOffMessage(val midiChannel: Int, val key: Int, val velocity: Int) : BinaryMarshaller {
    override fun toByteArray(): ByteArray {
        val bytes = ByteArray(3)
        bytes[0] = (MidiStatusBytes.NOTE_OFF.value + midiChannel).toByte()
        bytes[1] = key.toByte()
        bytes[2] = velocity.toByte()
        return bytes
    }
}
data class NoteOnMessage(val midiChannel: Int, val key: Int, val velocity: Int) : BinaryMarshaller {
    override fun toByteArray(): ByteArray {
        val bytes = ByteArray(3)
        bytes[0] = (MidiStatusBytes.NOTE_ON.value + midiChannel).toByte()
        bytes[1] = key.toByte()
        bytes[2] = velocity.toByte()
        return bytes
    }
}

data class PolyphonicKeyPressureMessage(val midiChannel: Int, val key: Int, val velocity: Int) : BinaryMarshaller {
    override fun toByteArray(): ByteArray {
        val bytes = ByteArray(3)
        bytes[0] = (MidiStatusBytes.POLYPHONIC_KEY_PRESSURE.value + midiChannel).toByte()
        bytes[1] = key.toByte()
        bytes[2] = velocity.toByte()
        return bytes
    }
}

data class ControlChangeMessage(val midiChannel: Int, val controllerNumber: Int, val controllerValue: Int) : BinaryMarshaller {
    override fun toByteArray(): ByteArray {
        val bytes = ByteArray(3)
        bytes[0] = (MidiStatusBytes.CONTROL_CHANGE.value + midiChannel).toByte()
        bytes[1] = controllerNumber.toByte()
        bytes[2] = controllerValue.toByte()
        return bytes
    }
}

data class ProgramChangeMessage(val midiChannel: Int, val programNumber: Int) : BinaryMarshaller {
    override fun toByteArray(): ByteArray {
        val bytes = ByteArray(2)
        bytes[0] = (MidiStatusBytes.PROGRAM_CHANGE.value + midiChannel).toByte()
        bytes[1] = programNumber.toByte()
        return bytes
    }
}

data class ChannelPressureMessage(val midiChannel: Int, val pressureValue: Int) : BinaryMarshaller {
    override fun toByteArray(): ByteArray {
        val bytes = ByteArray(2)
        bytes[0] = (MidiStatusBytes.CHANNEL_PRESSURE.value + midiChannel).toByte()
        bytes[1] = pressureValue.toByte()
        return bytes
    }
}

data class PitchBendMessage(val midiChannel: Int, val leastSignificantBits: Int, val mostSignificantBits: Int) : BinaryMarshaller {
    override fun toByteArray(): ByteArray {
        val bytes = ByteArray(3)
        bytes[0] = (MidiStatusBytes.PITCH_BEND_CHANGE.value + midiChannel).toByte()
        bytes[1] = leastSignificantBits.toByte()
        bytes[2] = mostSignificantBits.toByte()
        return bytes
    }
}

data class ChannelModeMessage(val midiChannel: Int, val controllerNumber: Int, val controllerValue: Int) : BinaryMarshaller {
    override fun toByteArray(): ByteArray {
        val bytes = ByteArray(3)
        bytes[0] = (MidiStatusBytes.CHANNEL_MODE.value + midiChannel).toByte()
        bytes[1] = controllerNumber.toByte()
        bytes[2] = controllerValue.toByte()
        return bytes
    }

    val isAllSoundOff: Boolean = controllerNumber == 120 && controllerValue == 0
    val isResetAllControllers: Boolean = controllerNumber == 121
    val isLocalControlOff: Boolean = controllerNumber == 122 && controllerValue == 0
    val isLocalControlOn: Boolean = controllerNumber == 122 && controllerValue == 127
    val isAllNotesOff: Boolean = controllerNumber == 123 && controllerValue == 0
    val isOmniModeOff: Boolean = controllerNumber == 124 && controllerValue == 0
    val isOmniModeOn: Boolean = controllerNumber == 125 && controllerValue == 0
    val isMonoModeOn: Boolean = controllerNumber == 126
    val isPloyModeOn: Boolean = controllerNumber == 127 && controllerValue == 0
}
data class SystemExclusiveMessage(val manufacturerID: ByteArray, val data: ByteArray) : BinaryMarshaller {
    override fun toByteArray(): ByteArray {
        var bytes = ByteArray(1 + manufacturerID.size + data.size + 1)
        bytes[0] = MidiStatusBytes.SYSTEM_EXCLUSIVE.value
        bytes += manufacturerID
        bytes += data
        bytes += MidiStatusBytes.SYSTEM_EXCLUSIVE_END.value
        return bytes
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SystemExclusiveMessage

        if (!manufacturerID.contentEquals(other.manufacturerID)) return false
        if (!data.contentEquals(other.data)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = manufacturerID.contentHashCode()
        result = 31 * result + data.contentHashCode()
        return result
    }
}
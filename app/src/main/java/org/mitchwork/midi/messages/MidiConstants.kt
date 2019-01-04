package org.mitchwork.midi.messages

enum class MidiStatusBytes(val value: Byte) {
    // Channel Voice Messages
    NOTE_OFF(0b10000000.toByte()),
    NOTE_ON(0b10010000.toByte()),
    POLYPHONIC_KEY_PRESSURE(0b10100000.toByte()),
    CONTROL_CHANGE(0b10110000.toByte()),
    PROGRAM_CHANGE(0b11000000.toByte()),
    CHANNEL_PRESSURE(0b11010000.toByte()),
    PITCH_BEND_CHANGE(0b11100000.toByte()),
    // Channel Mode Messages
    CHANNEL_MODE(0b10110000.toByte()),
    // System Common Messages
    SYSTEM_EXCLUSIVE(0b11110000.toByte()),
    SYSTEM_EXCLUSIVE_END(0b11110111.toByte()),
    MIDI_TIME_CODE_QUARTER_FRAME(0b11110001.toByte()),
    SONG_POSITION_POINTER(0b11110010.toByte()),
    SONG_SELECT(0b11110011.toByte()),
    TUNE_REQUEST(0b11110110.toByte()),
    // System Real-Time Messages
    TIMING_CLOCK(0b11111000.toByte()),
    START(0b11111010.toByte()),
    CONTINUE(0b11111011.toByte()),
    STOP(0b11111100.toByte()),
    ACTIVE_SENSING(0b11111110.toByte()),
    RESET(0b11111111.toByte()),

    UNKNOWN(0b00000000.toByte());

    fun isChannelVoiceMessage(): Boolean = value < CHANNEL_MODE.value
    fun isChannelModeMessage(): Boolean = value >= CHANNEL_MODE.value && value < SYSTEM_EXCLUSIVE.value
    fun isSystemCommonMessage(): Boolean = value >= SYSTEM_EXCLUSIVE.value && value < TIMING_CLOCK.value
    fun isSystemRealTimeMessage(): Boolean = value >= TIMING_CLOCK.value && value <= RESET.value

    companion object {
        fun fromByte(b: Byte): MidiStatusBytes {
            if (b < SYSTEM_EXCLUSIVE.value) {
                val midiChannel = b.toUByte().rem(0b00010000.toUByte())
                val withoutChannel = b.minus(midiChannel.toByte()).toByte()
                return values().find { midiStatusBytes -> midiStatusBytes.value == withoutChannel } ?: UNKNOWN
            }
            return values().find {midiStatusBytes -> midiStatusBytes.value == b } ?: UNKNOWN
        }
    }
}
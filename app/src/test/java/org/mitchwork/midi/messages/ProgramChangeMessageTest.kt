package org.mitchwork.midi.messages

import org.junit.Assert.assertTrue
import org.junit.Test

class ProgramChangeMessageTest {

    @Test
    fun toByteArray() {
        val pc = ProgramChangeMessage(12, 22)

        val bytes = pc.toByteArray()
        val expected = byteArrayOf(0xCC.toByte(), 0x16.toByte())

        assertTrue("ProgramChange message to bytes", expected.contentEquals(bytes))
    }
}
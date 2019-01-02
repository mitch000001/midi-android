package org.mitchwork.midi.messages

import org.junit.Assert
import org.junit.Test

class ControlChangeMessageTest {

    @Test
    fun toByteArray() {
        val cc = ControlChangeMessage(12, 22, 125)

        val bytes = cc.toByteArray()
        val expected = byteArrayOf(0xBC.toByte(), 0x16.toByte(), 0x7D.toByte())

        Assert.assertTrue("ControlChange message to bytes", expected.contentEquals(bytes))
    }
}
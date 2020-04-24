package com.ewingsa.ohyeah.messages

import org.junit.Assert.assertEquals
import org.junit.Test

class MessagesFragmentTest {

    @Test
    fun testGetSenderId() {
        val messagesFragment = MessagesFragment.newInstance(SENDER_ID)

        assertEquals(SENDER_ID, messagesFragment.getSenderId())
    }

    private companion object {
        const val SENDER_ID = 2L
    }
}

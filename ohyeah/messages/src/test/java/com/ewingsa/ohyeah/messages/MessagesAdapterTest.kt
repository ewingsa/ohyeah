package com.ewingsa.ohyeah.messages

import com.nhaarman.mockitokotlin2.mock
import org.junit.Assert.assertEquals
import org.junit.Test

class MessagesAdapterTest {

    @Test
    fun testGetItemCount() {
        val messagesAdapter = MessagesAdapter(listOf(mock(), mock()))

        assertEquals(NUMBER_OF_MESSAGES, messagesAdapter.itemCount)
    }

    private companion object {
        const val NUMBER_OF_MESSAGES = 2
    }
}

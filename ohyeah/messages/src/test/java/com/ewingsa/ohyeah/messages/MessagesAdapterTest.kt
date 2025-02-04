package com.ewingsa.ohyeah.messages

import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.mock

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

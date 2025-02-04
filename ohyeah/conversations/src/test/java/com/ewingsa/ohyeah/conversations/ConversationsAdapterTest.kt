package com.ewingsa.ohyeah.conversations

import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.mock

class ConversationsAdapterTest {

    @Test
    fun testGetItemCount() {
        val messagesAdapter = ConversationsAdapter(listOf(mock(), mock()))

        assertEquals(NUMBER_OF_MESSAGES, messagesAdapter.itemCount)
    }

    private companion object {
        const val NUMBER_OF_MESSAGES = 2
    }
}

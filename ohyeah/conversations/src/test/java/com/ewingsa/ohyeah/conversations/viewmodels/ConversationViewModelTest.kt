package com.ewingsa.ohyeah.conversations.viewmodels

import com.ewingsa.ohyeah.conversations.datamodels.ConversationDataModel
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.Before
import org.junit.Test

class ConversationViewModelTest {

    private val interactions: ConversationViewModel.Interactions = mock()

    private lateinit var conversationViewModel: ConversationViewModel

    @Before
    fun setup() {
        conversationViewModel = ConversationViewModel(CONVERSATION_DATA_MODEL, interactions)
    }

    @Test
    fun testOnPress() {
        conversationViewModel.onPress()

        verify(interactions).onConversationSelected(SENDER_ID)
    }

    @Test
    fun testOnLongPress() {
        conversationViewModel.onLongPress()

        verify(interactions).onDeleteConversationRequested(SENDER_ID)
    }

    private companion object {
        const val DISPLAY_TIME = "12:05 P.M."
        const val MESSAGE = "message"
        const val SENDER = "sender"
        const val SENDER_ID = 2L

        val CONVERSATION_DATA_MODEL = ConversationDataModel(SENDER_ID, SENDER, MESSAGE, DISPLAY_TIME, null, null)
    }
}

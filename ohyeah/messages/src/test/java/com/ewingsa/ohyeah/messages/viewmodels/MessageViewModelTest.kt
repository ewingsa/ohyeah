package com.ewingsa.ohyeah.messages.viewmodels

import com.ewingsa.ohyeah.messages.datamodels.DateLabelDataModel
import com.ewingsa.ohyeah.messages.datamodels.MessageDataModel
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import org.junit.Test

class MessageViewModelTest {

    private val interactions: MessageViewModel.Interactions = mock()

    @Test
    fun testOnLongPress_message() {
        val messagesScreenDataModel = MessageDataModel(MESSAGE_ID, MESSAGE, DISPLAY_TIME, null)

        val messagesScreenViewModel = MessageViewModel(messagesScreenDataModel, interactions)

        messagesScreenViewModel.onLongPress()

        verify(interactions).onEditMessageRequested(MESSAGE_ID)
    }

    @Test
    fun testOnLongPress_dateLabel() {
        val dateLabelDataModel: DateLabelDataModel = mock()

        val messagesScreenViewModel = MessageViewModel(dateLabelDataModel, interactions)

        messagesScreenViewModel.onLongPress

        verify(interactions, never()).onEditMessageRequested(any())
    }

    private companion object {
        const val DISPLAY_TIME = "12:05 P.M."
        const val MESSAGE = "message"
        const val MESSAGE_ID = 1L
    }
}

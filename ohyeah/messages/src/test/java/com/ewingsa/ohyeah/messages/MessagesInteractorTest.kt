package com.ewingsa.ohyeah.messages

import com.ewingsa.ohyeah.database.Message
import com.ewingsa.ohyeah.database.Sender
import com.ewingsa.ohyeah.database.SenderMessage
import com.ewingsa.ohyeah.helpers.DateHelper
import com.ewingsa.ohyeah.messages.datamodels.DateLabelDataModel
import com.ewingsa.ohyeah.messages.datamodels.MessageDataModel
import com.ewingsa.ohyeah.messages.datamodels.MessageItem
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class MessagesInteractorTest {

    private var presenter: MessagesContract.Presenter = mock()
    private var repository: MessagesContract.Repository = mock()
    private var router: MessagesContract.Router = mock()

    private lateinit var messagesInteractor: MessagesInteractor

    @Before
    fun setup() {
        messagesInteractor = MessagesInteractor(repository).apply {
            setPresenter(presenter)
            onRouterAttached(router)
        }
    }

    @Test
    fun testGoBack() {
        messagesInteractor.goBack()

        verify(router).goBack()
    }

    @Test
    fun testOnNewMessage() {
        messagesInteractor.onNewMessage(SENDER_ID)

        verify(router).goToSetNewReminder(SENDER_ID)
    }

    @Test
    fun testFetchMessages() {
        messagesInteractor.fetchMessages(SENDER_ID)

        verify(repository).fetchMessages(SENDER_ID)
    }

    @Test
    fun testOnMessagesRetrieved_messages() {
        val dateHelper: DateHelper = mock()

        whenever(dateHelper.formatDate(FIRST_DISPLAY_TIMESTAMP)).thenReturn(FIRST_DISPLAY_DATE)
        whenever(dateHelper.formatDate(SECOND_DISPLAY_TIMESTAMP)).thenReturn(SECOND_DISPLAY_DATE)
        whenever(dateHelper.formatTime(FIRST_DISPLAY_TIMESTAMP)).thenReturn(FIRST_DISPLAY_TIME)
        whenever(dateHelper.formatTime(SECOND_DISPLAY_TIMESTAMP)).thenReturn(SECOND_DISPLAY_TIME)
        whenever(dateHelper.now()).thenReturn(NOW)

        messagesInteractor.drawableHelper = mock()
        messagesInteractor.dateHelper = dateHelper

        messagesInteractor.onMessagesRetrieved(listOf(SENDER_MESSAGE_2, SENDER_MESSAGE)) // Messages are added from the bottom to the top

        val captor = argumentCaptor<List<MessageItem>>()

        verify(presenter).addMessages(captor.capture())

        assertEquals(MESSAGE_ID_2, (captor.firstValue[0] as MessageDataModel).id)
        assertEquals(SECOND_DISPLAY_DATE, (captor.firstValue[1] as DateLabelDataModel).displayDate)
        assertEquals(MESSAGE_ID, (captor.firstValue[2] as MessageDataModel).id)
        assertEquals(FIRST_DISPLAY_DATE, (captor.firstValue[3] as DateLabelDataModel).displayDate)

        verify(presenter).scrollToPosition(MOST_RECENT_MESSAGE_POSITION)
        verify(repository).markReadMessages(SENDER_ID, NOW)
    }

    @Test
    fun testOnMessagesRetrieved_noMessages() {
        messagesInteractor.onMessagesRetrieved(emptyList())

        verify(router).goBack()
    }

    @Test
    fun testOnEditMessage() {
        messagesInteractor.onEditMessage(MESSAGE_ID)

        verify(router).goToUpdateReminder(MESSAGE_ID)
    }

    @Test
    fun testClear() {
        messagesInteractor.clear()

        verify(repository).clear()
    }

    private companion object {
        const val FIRST_DISPLAY_DATE = "Fri, Apr 17, 2020"
        const val FIRST_DISPLAY_TIME = "1:05 PM"
        const val FIRST_DISPLAY_TIMESTAMP = 1587128700000L
        const val MESSAGE_TEXT = "message"
        const val MESSAGE_ID = 1L
        const val MESSAGE_ID_2 = 11L
        const val MOST_RECENT_MESSAGE_POSITION = 2 // 0 is the position of the pseudo-upcoming message. 1 is the position of its date label.
        const val NOW = 1587198600000L // Sat, Apr 18, 2020, 1:30 AM
        const val SECOND_DISPLAY_DATE = "Sat, Apr 18, 2020"
        const val SECOND_DISPLAY_TIME = "1:50 PM"
        const val SECOND_DISPLAY_TIMESTAMP = 1587217800000L
        const val SENDER_NAME = "sender"
        const val SENDER_ID = 2L

        val SENDER = Sender(SENDER_ID, SENDER_NAME, null)
        val SENDER_MESSAGE = SenderMessage(SENDER, Message(MESSAGE_ID, MESSAGE_TEXT, SENDER_ID, FIRST_DISPLAY_TIMESTAMP))
        val SENDER_MESSAGE_2 = SenderMessage(SENDER, Message(MESSAGE_ID_2, MESSAGE_TEXT, SENDER_ID, SECOND_DISPLAY_TIMESTAMP))
    }
}

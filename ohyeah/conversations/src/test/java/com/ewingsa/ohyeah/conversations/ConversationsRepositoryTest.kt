package com.ewingsa.ohyeah.conversations

import com.ewingsa.ohyeah.database.Message
import com.ewingsa.ohyeah.database.MessageDao
import com.ewingsa.ohyeah.database.PreviewSenderMessage
import com.ewingsa.ohyeah.database.Sender
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers.trampoline
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ConversationsRepositoryTest {

    private var interactor: ConversationsContract.Interactor = mock()
    private var messageDao: MessageDao = mock()

    private lateinit var conversationsRepository: ConversationsRepository

    @Before
    fun setup() {
        conversationsRepository = ConversationsRepository(messageDao, trampoline(), trampoline()).apply {
            setInteractor(interactor)
        }
    }

    @Test
    fun testFetchConversations_allPrevious() {
        val previousConversationsObservable = Observable.fromArray(listOf(PREVIEW_SENDER_MESSAGE, PREVIEW_SENDER_MESSAGE_2))

        whenever(messageDao.getPreviousConversations(NOW)).thenReturn(previousConversationsObservable)
        whenever(messageDao.getFutureConversations(NOW)).thenReturn(Observable.fromArray(emptyList()))

        conversationsRepository.fetchConversations(NOW)

        val captor = argumentCaptor<List<PreviewSenderMessage>>()

        verify(interactor).onConversationsRetrieved(captor.capture())

        assertEquals(MESSAGE_ID, captor.firstValue[0].message.messageId)
        assertEquals(MESSAGE_ID_2, captor.firstValue[1].message.messageId)
    }

    @Test
    fun testFetchConversations_previousAndFuture() {
        val previousConversationsObservable = Observable.fromArray(listOf(PREVIEW_SENDER_MESSAGE))
        val futureConversationsObservable = Observable.fromArray(listOf(PREVIEW_SENDER_MESSAGE_3))

        whenever(messageDao.getPreviousConversations(NOW)).thenReturn(previousConversationsObservable)
        whenever(messageDao.getFutureConversations(NOW)).thenReturn(futureConversationsObservable)

        conversationsRepository.fetchConversations(NOW)

        val captor = argumentCaptor<List<PreviewSenderMessage>>()

        verify(interactor).onConversationsRetrieved(captor.capture())

        assertEquals(MESSAGE_ID, captor.firstValue[0].message.messageId)
        assertEquals(MESSAGE_ID_3, captor.firstValue[1].message.messageId)
    }

    @Test
    fun testFetchConversations_oneConversationHasPreviousAndFuture() {
        val previousConversationsObservable = Observable.fromArray(listOf(PREVIEW_SENDER_MESSAGE_2))
        val futureConversationsObservable = Observable.fromArray(listOf(PREVIEW_SENDER_MESSAGE_3))

        whenever(messageDao.getPreviousConversations(NOW)).thenReturn(previousConversationsObservable)
        whenever(messageDao.getFutureConversations(NOW)).thenReturn(futureConversationsObservable)

        conversationsRepository.fetchConversations(NOW)

        val captor = argumentCaptor<List<PreviewSenderMessage>>()

        verify(interactor).onConversationsRetrieved(captor.capture())

        assertEquals(SIZE_OF_CONVERSATIONS_WHEN_ONE_DISTINCT, captor.firstValue.size)
        assertEquals(MESSAGE_ID_2, captor.firstValue[0].message.messageId)
    }

    @Test
    fun testFetchMessages() {
        val messagesObservable = Observable.fromArray(listOf(MESSAGE, MESSAGE_2))

        whenever(messageDao.findMessages(SENDER_ID)).thenReturn(messagesObservable)

        conversationsRepository.fetchMessages(SENDER_ID)

        val captor = argumentCaptor<List<Message>>()

        verify(interactor).onMessagesRetrieved(captor.capture())

        assertEquals(MESSAGE_ID, captor.firstValue[0].messageId)
        assertEquals(MESSAGE_ID_2, captor.firstValue[1].messageId)
    }

    private companion object {
        const val FIRST_PREVIOUS_MESSAGE_TIMESTAMP = 1587128700000L // Fri, Apr 17, 2020, 1:05 PM
        const val FUTURE_MESSAGE_TIMESTAMP = 1587217800000L // Sat, Apr 18, 2020, 1:50 PM
        const val SECOND_PREVIOUS_MESSAGE_TIMESTAMP = 1587164700000L // Fri, Apr 17, 2020, 11:05 PM
        const val MESSAGE_TEXT = "message"
        const val MESSAGE_ID = 1L
        const val MESSAGE_ID_2 = 11L
        const val MESSAGE_ID_3 = 111L
        const val NOW = 1587198600000L // Sat, Apr 18, 2020, 8:30 AM
        const val SENDER_NAME = "sender"
        const val SENDER_ID = 2L
        const val SENDER_ID_2 = 22L
        const val SIZE_OF_CONVERSATIONS_WHEN_ONE_DISTINCT = 1

        val MESSAGE = Message(MESSAGE_ID, MESSAGE_TEXT, SENDER_ID, FIRST_PREVIOUS_MESSAGE_TIMESTAMP)
        val MESSAGE_2 = Message(MESSAGE_ID_2, MESSAGE_TEXT, SENDER_ID, SECOND_PREVIOUS_MESSAGE_TIMESTAMP)
        val MESSAGE_3 = Message(MESSAGE_ID_3, MESSAGE_TEXT, SENDER_ID, FUTURE_MESSAGE_TIMESTAMP)
        val SENDER = Sender(SENDER_ID, SENDER_NAME, null)
        val SENDER_2 = Sender(SENDER_ID_2, SENDER_NAME, null)
        val PREVIEW_SENDER_MESSAGE = PreviewSenderMessage(SENDER, MESSAGE)
        val PREVIEW_SENDER_MESSAGE_2 = PreviewSenderMessage(SENDER_2, MESSAGE_2)
        val PREVIEW_SENDER_MESSAGE_3 = PreviewSenderMessage(SENDER_2, MESSAGE_3)
    }
}

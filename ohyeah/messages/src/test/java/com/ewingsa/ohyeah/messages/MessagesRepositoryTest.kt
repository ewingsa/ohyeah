package com.ewingsa.ohyeah.messages

import com.ewingsa.ohyeah.database.Message
import com.ewingsa.ohyeah.database.MessageDao
import com.ewingsa.ohyeah.database.Sender
import com.ewingsa.ohyeah.database.SenderMessage
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers.trampoline
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class MessagesRepositoryTest {

    private var interactor: MessagesContract.Interactor = mock()
    private var messageDao: MessageDao = mock()

    private lateinit var messagesRepository: MessagesRepository

    @Before
    fun setup() {
        messagesRepository = MessagesRepository(messageDao, trampoline(), trampoline()).apply {
            setInteractor(interactor)
        }
    }

    @Test
    fun testFetchMessages() {
        val senderMaybe = Maybe.just(SENDER)
        val messagesObservable = Observable.fromArray(listOf(MESSAGE, MESSAGE_2))

        whenever(messageDao.getExistingConversation(SENDER_ID)).thenReturn(senderMaybe)
        whenever(messageDao.findMessages(SENDER_ID)).thenReturn(messagesObservable)

        messagesRepository.fetchMessages(SENDER_ID)

        val captor = argumentCaptor<List<SenderMessage>>()

        verify(interactor).onMessagesRetrieved(captor.capture())

        assertEquals(MESSAGE_ID, captor.firstValue[0].message.messageId)
        assertEquals(MESSAGE_ID_2, captor.firstValue[1].message.messageId)
        assertEquals(SENDER_NAME, captor.firstValue[0].sender.name)
    }

    private companion object {
        const val MESSAGE_TEXT = "message"
        const val MESSAGE_ID = 1L
        const val MESSAGE_ID_2 = 11L
        const val SENDER_NAME = "sender"
        const val SENDER_ID = 2L

        val MESSAGE = Message(MESSAGE_ID, MESSAGE_TEXT, SENDER_ID, 3L)
        val MESSAGE_2 = Message(MESSAGE_ID_2, MESSAGE_TEXT, SENDER_ID, 3L)
        val SENDER = Sender(SENDER_ID, SENDER_NAME, null)
    }
}

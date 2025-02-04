package com.ewingsa.ohyeah.setreminder

import com.ewingsa.ohyeah.database.Message
import com.ewingsa.ohyeah.database.MessageDao
import com.ewingsa.ohyeah.database.Sender
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.schedulers.Schedulers.trampoline
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class SetReminderRepositoryTest {

    private var interactor: SetReminderContract.Interactor = mock()
    private var messageDao: MessageDao = mock()

    private lateinit var setReminderRepository: SetReminderRepository

    @Before
    fun setup() {
        setReminderRepository = SetReminderRepository(messageDao, trampoline(), trampoline()).apply {
            setInteractor(interactor)
        }
    }

    @Test
    fun testGetExistingReminder_messageExists() {
        val messageMaybe = Maybe.just(MESSAGE)
        val senderMaybe = Maybe.just(SENDER)

        whenever(messageDao.findMessage(MESSAGE_ID)).thenReturn(messageMaybe)
        whenever(messageDao.getExistingConversation(SENDER_ID)).thenReturn(senderMaybe)

        setReminderRepository.getExistingReminder(MESSAGE_ID)

        verify(interactor).onReminderLoaded(MESSAGE, SENDER)
    }

    @Test
    fun testGetExistingReminder_messageDoesNotExists() {
        val messageMaybe = Maybe.empty<Message>()

        whenever(messageDao.findMessage(MESSAGE_ID)).thenReturn(messageMaybe)

        setReminderRepository.getExistingReminder(MESSAGE_ID)

        verify(interactor, never()).onReminderLoaded(any(), any())
    }

    @Test
    fun testGetExistingReminder_senderDoesNotExists() {
        val messageMaybe = Maybe.just(MESSAGE)
        val senderMaybe = Maybe.empty<Sender>()

        whenever(messageDao.findMessage(MESSAGE_ID)).thenReturn(messageMaybe)
        whenever(messageDao.getExistingConversation(SENDER_ID)).thenReturn(senderMaybe)

        setReminderRepository.getExistingReminder(MESSAGE_ID)

        verify(interactor, never()).onReminderLoaded(any(), any())
    }

    @Test
    fun testGetExistingSender_senderExists() {
        val senderMaybe = Maybe.just(SENDER)

        whenever(messageDao.getExistingConversation(SENDER_ID)).thenReturn(senderMaybe)

        setReminderRepository.getExistingSender(SENDER_ID)

        verify(interactor).onSenderLoaded(SENDER)
    }

    @Test
    fun testGetExistingSender_senderDoesNotExist() {
        val senderMaybe = Maybe.empty<Sender>()

        whenever(messageDao.getExistingConversation(SENDER_ID)).thenReturn(senderMaybe)

        setReminderRepository.getExistingSender(SENDER_ID)

        verify(interactor, never()).onSenderLoaded(any())
    }

    @Test
    fun testGetExistingConversation_conversationExists() {
        val senderMaybe = Maybe.just(SENDER)

        whenever(messageDao.getExistingConversation(SENDER_NAME)).thenReturn(senderMaybe)

        setReminderRepository.getExistingConversation(SENDER_NAME)

        verify(interactor).onSenderSaved(SENDER_ID)
    }

    @Test
    fun testGetExistingConversation_conversationDoesNotExist() {
        val senderMaybe = Maybe.empty<Sender>()

        whenever(messageDao.getExistingConversation(SENDER_NAME)).thenReturn(senderMaybe)

        setReminderRepository.getExistingConversation(SENDER_NAME)

        verify(interactor).onNewSender()
    }

    @Test
    fun testSaveNewSender() {
        whenever(messageDao.insertConversation(SENDER)).thenReturn(SENDER_ID)

        setReminderRepository.saveNewSender(SENDER)

        verify(interactor).onSenderSaved(SENDER_ID)
    }

    @Test
    fun testSaveNewMessage() {
        whenever(messageDao.insertMessage(MESSAGE)).thenReturn(MESSAGE_ID)

        setReminderRepository.saveNewMessage(MESSAGE)

        verify(interactor).onMessageSaved(MESSAGE_ID)
    }

    @Test
    fun testUpdateMessage() {
        val complete = Completable.complete()

        whenever(messageDao.updateMessage(MESSAGE)).thenReturn(complete)

        setReminderRepository.updateMessage(MESSAGE)

        verify(interactor).onMessageUpdated()
    }

    @Test
    fun testDeleteReminder() {
        val complete = Completable.complete()

        whenever(messageDao.deleteMessage(MESSAGE_ID)).thenReturn(complete)

        setReminderRepository.deleteReminder(MESSAGE_ID)

        verify(interactor).onReminderDeleted()
    }

    @Test
    fun testIsSenderOrphaned_isOrphaned() {
        val resultMaybe = Maybe.just(true)

        whenever(messageDao.isConversationEmpty(SENDER_ID)).thenReturn(resultMaybe)

        setReminderRepository.isSenderOrphaned(SENDER_ID)

        verify(interactor).onSenderOrphaned(true)
    }

    @Test
    fun testIsSenderOrphaned_notOrphaned() {
        val resultMaybe = Maybe.just(false)

        whenever(messageDao.isConversationEmpty(SENDER_ID)).thenReturn(resultMaybe)

        setReminderRepository.isSenderOrphaned(SENDER_ID)

        verify(interactor).onSenderOrphaned(false)
    }

    @Test
    fun testIsSenderOrphaned_noResult() {
        val resultMaybe = Maybe.empty<Boolean>()

        whenever(messageDao.isConversationEmpty(SENDER_ID)).thenReturn(resultMaybe)

        setReminderRepository.isSenderOrphaned(SENDER_ID)

        verify(interactor, never()).onSenderOrphaned(any())
    }

    @Test
    fun testDeleteSender() {
        val complete = Completable.complete()

        whenever(messageDao.deleteSender(SENDER_ID)).thenReturn(complete)

        setReminderRepository.deleteSender(SENDER_ID)

        verify(interactor).onSenderDeleted()
    }

    private companion object {
        const val MESSAGE_ID = 1L
        const val SENDER_ID = 2L
        const val SENDER_NAME = "sender"
        const val TIMESTAMP = 3L

        val MESSAGE = Message(MESSAGE_ID, "", SENDER_ID, TIMESTAMP)
        val SENDER = Sender(SENDER_ID, SENDER_NAME, null)
    }
}

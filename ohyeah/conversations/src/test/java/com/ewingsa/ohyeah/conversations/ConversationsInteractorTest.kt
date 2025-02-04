package com.ewingsa.ohyeah.conversations

import android.content.Context
import android.content.res.Resources
import com.ewingsa.ohyeah.conversations.datamodels.ConversationDataModel
import com.ewingsa.ohyeah.conversations.datamodels.ConversationItem
import com.ewingsa.ohyeah.conversations.datamodels.UpcomingLabelDataModel
import com.ewingsa.ohyeah.database.Message
import com.ewingsa.ohyeah.database.PreviewSenderMessage
import com.ewingsa.ohyeah.database.Sender
import com.ewingsa.ohyeah.helpers.DateHelper
import com.ewingsa.ohyeah.helpers.IntentHelper
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class ConversationsInteractorTest {

    private var presenter: ConversationsContract.Presenter = mock()
    private var repository: ConversationsContract.Repository = mock()
    private var router: ConversationsContract.Router = mock()
    private var resources: Resources = mock()

    private lateinit var conversationsInteractor: ConversationsInteractor

    @Before
    fun setup() {
        conversationsInteractor = ConversationsInteractor(repository, resources).apply {
            setPresenter(presenter)
            onRouterAttached(router)
        }
    }

    @Test
    fun testGoToInfo() {
        conversationsInteractor.goToInfo()

        verify(router).goToInfoScreen()
    }

    @Test
    fun testGoToSetReminder() {
        conversationsInteractor.goToSetReminder()

        verify(router).goToSetReminderScreen()
    }

    @Test
    fun testFetchConversations() {
        conversationsInteractor.fetchConversations()

        verify(repository).fetchConversations(any())
    }

    @Test
    fun testOnConversationsRetrieved_noConversations() {
        conversationsInteractor.onConversationsRetrieved(emptyList())

        verify(presenter).addNoConversationsMessage()
    }

    @Test
    fun testOnConversationsRetrieved_withUpcoming() {
        val dateHelper: DateHelper = mock()

        whenever(dateHelper.now()).thenReturn(NOW)

        conversationsInteractor.dateHelper = dateHelper

        val previewSenderMessage = PreviewSenderMessage(SENDER, Message(MESSAGE_ID, MESSAGE_TEXT, SENDER_ID, FIRST_MESSAGE_TIMESTAMP))
        val secondPreviewSenderMessage = PreviewSenderMessage(SENDER_2, Message(MESSAGE_ID_2, MESSAGE_TEXT, SENDER_ID_2, SECOND_MESSAGE_TIMESTAMP))

        conversationsInteractor.onConversationsRetrieved(listOf(previewSenderMessage, secondPreviewSenderMessage))

        val captor = argumentCaptor<List<ConversationItem>>()

        verify(presenter).addConversations(captor.capture())

        assertEquals(SENDER_ID, (captor.firstValue[0] as ConversationDataModel).senderId)
        assertNotNull(captor.firstValue[1] as UpcomingLabelDataModel)
        assertEquals(SENDER_ID_2, (captor.firstValue[2] as ConversationDataModel).senderId)
    }

    @Test
    fun testOnConversationsRetrieved_noUnread() {
        val dateHelper: DateHelper = mock()

        whenever(dateHelper.now()).thenReturn(NOW)

        conversationsInteractor.dateHelper = dateHelper

        val previewSenderMessage = PreviewSenderMessage(SENDER, Message(MESSAGE_ID, MESSAGE_TEXT, SENDER_ID, FIRST_MESSAGE_TIMESTAMP), NO_UNREAD)

        conversationsInteractor.onConversationsRetrieved(listOf(previewSenderMessage))

        val captor = argumentCaptor<List<ConversationItem>>()

        verify(presenter).addConversations(captor.capture())

        assertNull((captor.firstValue[0] as ConversationDataModel).numberUnread)
    }

    @Test
    fun testOnConversationsRetrieved_twoUnread() {
        val dateHelper: DateHelper = mock()

        whenever(dateHelper.now()).thenReturn(NOW)

        conversationsInteractor.dateHelper = dateHelper

        val previewSenderMessage = PreviewSenderMessage(SENDER, Message(MESSAGE_ID, MESSAGE_TEXT, SENDER_ID, FIRST_MESSAGE_TIMESTAMP), TWO_UNREAD)

        conversationsInteractor.onConversationsRetrieved(listOf(previewSenderMessage))

        val captor = argumentCaptor<List<ConversationItem>>()

        verify(presenter).addConversations(captor.capture())

        assertEquals(TWO_UNREAD.toString(), (captor.firstValue[0] as ConversationDataModel).numberUnread)
    }

    @Test
    fun testOnConversationsRetrieved_tenUnread() {
        val dateHelper: DateHelper = mock()

        whenever(dateHelper.now()).thenReturn(NOW)
        whenever(resources.getString(R.string.conversation_nine_plus)).thenReturn(NINE_PLUS_UNREAD)

        conversationsInteractor.dateHelper = dateHelper

        val previewSenderMessage = PreviewSenderMessage(SENDER, Message(MESSAGE_ID, MESSAGE_TEXT, SENDER_ID, FIRST_MESSAGE_TIMESTAMP), TEN_UNREAD)

        conversationsInteractor.onConversationsRetrieved(listOf(previewSenderMessage))

        val captor = argumentCaptor<List<ConversationItem>>()

        verify(presenter).addConversations(captor.capture())

        assertEquals(NINE_PLUS_UNREAD, (captor.firstValue[0] as ConversationDataModel).numberUnread)
    }

    @Test
    fun testGoToConversation() {
        conversationsInteractor.goToConversation(SENDER_ID)

        verify(router).goToMessagesScreen(SENDER_ID)
    }

    @Test
    fun testDeleteConversation() {
        conversationsInteractor.deleteConversation(SENDER_ID)

        verify(repository).fetchMessages(SENDER_ID)
    }

    @Test
    fun testOnMessagesRetrieved() {
        val context: Context = mock()
        val intentHelper: IntentHelper = mock()

        whenever(presenter.getContext()).thenReturn(context)

        conversationsInteractor.intentHelper = intentHelper

        conversationsInteractor.onMessagesRetrieved(listOf(MESSAGE))

        verify(intentHelper).deletePendingIntent(eq(context), any(), eq(MESSAGE_ID))
    }

    @Test
    fun testClear() {
        conversationsInteractor.clear()

        verify(repository).clear()
    }

    private companion object {
        const val FIRST_MESSAGE_TIMESTAMP = 1587128700000L // Fri, Apr 17, 2020, 1:05 PM
        const val MESSAGE_TEXT = "message"
        const val MESSAGE_ID = 1L
        const val MESSAGE_ID_2 = 11L
        const val NINE_PLUS_UNREAD = "9+"
        const val NO_UNREAD = 0
        const val NOW = 1587198600000L // Sat, Apr 18, 2020, 8:30 AM
        const val SECOND_MESSAGE_TIMESTAMP = 1587217800000L // Sat, Apr 18, 2020, 1:50 PM
        const val SENDER_NAME = "sender"
        const val SENDER_ID = 2L
        const val SENDER_ID_2 = 22L
        const val TEN_UNREAD = 10
        const val TWO_UNREAD = 2

        val MESSAGE = Message(MESSAGE_ID, MESSAGE_TEXT, SENDER_ID, FIRST_MESSAGE_TIMESTAMP)
        val SENDER = Sender(SENDER_ID, SENDER_NAME, null)
        val SENDER_2 = Sender(SENDER_ID_2, SENDER_NAME, null)
    }
}

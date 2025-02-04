package com.ewingsa.ohyeah.conversations

import android.content.Context
import com.ewingsa.ohyeah.conversations.datamodels.ConversationDataModel
import com.ewingsa.ohyeah.conversations.datamodels.UpcomingLabelDataModel
import com.ewingsa.ohyeah.conversations.viewmodels.ConversationViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class ConversationsPresenterTest {

    private var view: ConversationsContract.View = mock()
    private var interactor: ConversationsContract.Interactor = mock()
    private var router: ConversationsContract.Router = mock()

    private lateinit var conversationsPresenter: ConversationsPresenter

    @Before
    fun setup() {
        conversationsPresenter = ConversationsPresenter(interactor).apply {
            onViewAttached(view, router)
        }
    }

    @Test
    fun testOnViewAttached() {
        conversationsPresenter.onViewAttached(view, router)

        verify(interactor, times(2)).onRouterAttached(router)
        verify(view, times(2)).setScreenViewModel(any())
        verify(interactor, times(2)).fetchConversations()
    }

    @Test
    fun testAddNoConversationsMessage() {
        conversationsPresenter.addNoConversationsMessage()

        verify(view).addNoConversationsMessage()
    }

    @Test
    fun testGetContext() {
        val contextMock: Context = mock()

        whenever(view.getContext()).thenReturn(contextMock)

        val context = conversationsPresenter.getContext()

        assertEquals(contextMock, context)
    }

    @Test
    fun testAddConversations() {
        conversationsPresenter.addConversations(listOf(CONVERSATION_DATA_MODEL, UPCOMING_LABEL_DATA_MODEL, CONVERSATION_DATA_MODEL_2))

        val captor = argumentCaptor<List<ConversationViewModel>>()

        verify(view).addConversations(captor.capture())

        assertEquals(SENDER_ID, (captor.firstValue[0].conversationDataModel as ConversationDataModel).senderId)
        assertNotNull(captor.firstValue[1].conversationDataModel as UpcomingLabelDataModel)
        assertEquals(SENDER_ID_2, (captor.firstValue[2].conversationDataModel as ConversationDataModel).senderId)
    }

    @Test
    fun testOnInfoPress() {
        conversationsPresenter.onInfoPress()

        verify(interactor).goToInfo()
    }

    @Test
    fun testOnNewPress() {
        conversationsPresenter.onNewPress()

        verify(interactor).goToSetReminder()
    }

    @Test
    fun testOnConversationSelected() {
        conversationsPresenter.onConversationSelected(SENDER_ID)

        verify(interactor).goToConversation(SENDER_ID)
    }

    @Test
    fun testOnViewDetached() {
        conversationsPresenter.onViewDetached()

        verify(interactor).clear()
    }

    private companion object {
        const val DISPLAY_TIME = "12:05 P.M."
        const val PREVIEW_TEXT = "preview"
        const val SENDER = "sender"
        const val SENDER_ID = 2L
        const val SENDER_ID_2 = 22L

        val CONVERSATION_DATA_MODEL = ConversationDataModel(SENDER_ID, SENDER, PREVIEW_TEXT, DISPLAY_TIME, null, null)
        val CONVERSATION_DATA_MODEL_2 = ConversationDataModel(SENDER_ID_2, SENDER, PREVIEW_TEXT, DISPLAY_TIME, null, null)
        val UPCOMING_LABEL_DATA_MODEL = UpcomingLabelDataModel()
    }
}

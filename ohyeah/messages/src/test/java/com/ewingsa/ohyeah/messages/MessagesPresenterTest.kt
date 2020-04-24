package com.ewingsa.ohyeah.messages

import android.content.Context
import com.ewingsa.ohyeah.messages.datamodels.DateLabelDataModel
import com.ewingsa.ohyeah.messages.datamodels.MessageDataModel
import com.ewingsa.ohyeah.messages.viewmodels.MessageViewModel
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class MessagesPresenterTest {

    private var view: MessagesContract.View = mock()
    private var interactor: MessagesContract.Interactor = mock()
    private var router: MessagesContract.Router = mock()

    private lateinit var messagesPresenter: MessagesPresenter

    @Before
    fun setup() {
        whenever(view.getSenderId()).thenReturn(SENDER_ID)

        messagesPresenter = MessagesPresenter(interactor).apply {
            onViewAttached(view, router)
        }
    }

    @Test
    fun testOnViewAttached() {
        messagesPresenter.onViewAttached(view, router)

        verify(interactor, times(2)).onRouterAttached(router)
        verify(view, times(2)).setToolbarViewModel(any())
        verify(interactor, times(2)).fetchMessages(SENDER_ID)
    }

    @Test
    fun testGetContext() {
        val contextMock: Context = mock()

        whenever(view.getContext()).thenReturn(contextMock)

        val context = messagesPresenter.getContext()

        assertEquals(contextMock, context)
    }

    @Test
    fun testSetSenderName() {
        messagesPresenter.setSenderName(SENDER)

        verify(view).setSenderName(SENDER)
    }

    @Test
    fun testAddMessages() {
        // Messages are added from the bottom to the top
        messagesPresenter.addMessages(listOf(MESSAGE_DATA_MODEL_2, MESSAGE_DATA_MODEL, DATE_LABEL_DATA_MODEL))

        val captor = argumentCaptor<List<MessageViewModel>>()

        verify(view).addMessages(captor.capture())

        assertEquals(MESSAGE_ID_2, (captor.firstValue[0].messageDataModel as MessageDataModel).id)
        assertEquals(MESSAGE_ID, (captor.firstValue[1].messageDataModel as MessageDataModel).id)
        assertEquals(DISPLAY_DATE, (captor.firstValue[2].messageDataModel as DateLabelDataModel).displayDate)
    }

    @Test
    fun testScrollToPosition() {
        whenever(view.getMessageCount()).thenReturn(MESSAGE_COUNT)

        messagesPresenter.scrollToPosition(POSITION)

        verify(view).scrollToPosition(POSITION)
    }

    @Test
    fun testOnBackPress() {
        messagesPresenter.onBackPress()

        verify(interactor).goBack()
    }

    @Test
    fun testOnNewPress() {
        whenever(view.getSenderId()).thenReturn(SENDER_ID)

        messagesPresenter.onNewPress()

        verify(interactor).onNewMessage(SENDER_ID)
    }

    @Test
    fun testOnEditMessageRequest() {
        messagesPresenter.onEditMessageRequested(MESSAGE_ID)

        verify(interactor).onEditMessage(MESSAGE_ID)
    }

    @Test
    fun testOnViewDetached() {
        messagesPresenter.onViewDetached()

        verify(interactor).clear()
    }

    private companion object {
        const val DISPLAY_DATE = "Fri, Apr 17, 2020"
        const val DISPLAY_TIME = "12:05 P.M."
        const val MESSAGE = "message"
        const val MESSAGE_COUNT = 3
        const val MESSAGE_ID = 1L
        const val MESSAGE_ID_2 = 11L
        const val POSITION = 2
        const val SENDER = "sender"
        const val SENDER_ID = 2L

        val DATE_LABEL_DATA_MODEL = DateLabelDataModel(DISPLAY_DATE)
        val MESSAGE_DATA_MODEL = MessageDataModel(MESSAGE_ID, MESSAGE, DISPLAY_TIME, null)
        val MESSAGE_DATA_MODEL_2 = MessageDataModel(MESSAGE_ID_2, MESSAGE, DISPLAY_TIME, null)
    }
}

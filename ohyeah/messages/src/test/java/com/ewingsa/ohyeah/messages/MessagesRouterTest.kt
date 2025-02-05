package com.ewingsa.ohyeah.messages

import com.ewingsa.ohyeah.routing.RoutingHelper
import com.ewingsa.ohyeah.setreminder.SetReminderFragment
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.eq
import org.mockito.kotlin.isNull
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class MessagesRouterTest {

    private var fragment: MessagesFragment = mock()
    private var routingHelper: RoutingHelper = mock()

    private lateinit var messagesRouter: MessagesRouter

    @Before
    fun setup() {
        messagesRouter = MessagesRouter(fragment)
        messagesRouter.routingHelper = routingHelper
    }

    @Test
    fun testGoBack() {
        messagesRouter.goBack()

        verify(routingHelper).goBack(fragment)
    }

    @Test
    fun testGoToSetNewReminder() {
        messagesRouter.goToSetNewReminder(SENDER_ID)

        val captor = argumentCaptor<SetReminderFragment>()

        verify(routingHelper).goToScreen(eq(fragment), captor.capture(), isNull())

        assertEquals(SENDER_ID, captor.firstValue.getSenderId())
    }

    @Test
    fun testGoToUpdateReminder() {
        messagesRouter.goToUpdateReminder(MESSAGE_ID)

        val captor = argumentCaptor<SetReminderFragment>()

        verify(routingHelper).goToScreen(eq(fragment), captor.capture(), isNull())

        assertEquals(MESSAGE_ID, captor.firstValue.getMessageId())
    }

    private companion object {
        const val MESSAGE_ID = 1L
        const val SENDER_ID = 2L
    }
}

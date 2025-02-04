package com.ewingsa.ohyeah.conversations

import com.ewingsa.ohyeah.info.InfoFragment
import com.ewingsa.ohyeah.messages.MessagesFragment
import com.ewingsa.ohyeah.routing.RoutingHelper
import com.ewingsa.ohyeah.setreminder.SetReminderFragment
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.eq
import org.mockito.kotlin.isNull
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class ConversationsRouterTest {

    private var fragment: ConversationsFragment = mock()
    private var routingHelper: RoutingHelper = mock()

    private lateinit var conversationsRouter: ConversationsRouter

    @Before
    fun setup() {
        conversationsRouter = ConversationsRouter(fragment)
        conversationsRouter.routingHelper = routingHelper
    }

    @Test
    fun testGoToInfoScreen() {
        conversationsRouter.goToInfoScreen()

        verify(routingHelper).goToScreen(eq(fragment), any<InfoFragment>(), isNull())
    }

    @Test
    fun testGoToSetReminderScreen() {
        conversationsRouter.goToSetReminderScreen()

        verify(routingHelper).goToScreen(eq(fragment), any<SetReminderFragment>(), isNull())
    }

    @Test
    fun testGoToMessagesScreen() {
        conversationsRouter.goToMessagesScreen(SENDER_ID)

        val captor = argumentCaptor<MessagesFragment>()

        verify(routingHelper).goToScreen(eq(fragment), captor.capture(), isNull())

        assertEquals(SENDER_ID, captor.firstValue.getSenderId())
    }

    private companion object {
        const val SENDER_ID = 2L
    }
}

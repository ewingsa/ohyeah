package com.ewingsa.ohyeah.deeplink

import android.net.Uri
import androidx.fragment.app.FragmentManager
import com.ewingsa.ohyeah.messages.MessagesFragment
import com.ewingsa.ohyeah.routing.RoutingHelper
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.eq
import org.mockito.kotlin.isNull
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class DeeplinkResolverTest {

    @Test
    fun testHandleMessagesDeeplink() {
        val routingHelper: RoutingHelper = mock()
        val uri: Uri = mock()
        val fragmentManager: FragmentManager = mock()

        whenever(uri.toString()).thenReturn(SENDER_ID_STRING)

        DeeplinkResolver.routingHelper = routingHelper
        DeeplinkResolver.handleMessagesDeeplink(uri, fragmentManager)

        val captor = argumentCaptor<MessagesFragment>()

        verify(routingHelper).goToScreen(eq(fragmentManager), captor.capture(), isNull())

        assertEquals(SENDER_ID, captor.firstValue.getSenderId())
    }

    private companion object {
        const val SENDER_ID = 2L
        const val SENDER_ID_STRING = SENDER_ID.toString()
    }
}

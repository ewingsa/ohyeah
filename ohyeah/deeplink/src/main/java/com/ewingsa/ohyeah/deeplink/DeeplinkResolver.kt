package com.ewingsa.ohyeah.deeplink

import android.net.Uri
import androidx.annotation.VisibleForTesting
import androidx.fragment.app.FragmentManager
import com.ewingsa.ohyeah.messages.MessagesFragment
import com.ewingsa.ohyeah.routing.RoutingHelper

object DeeplinkResolver {

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    var routingHelper = RoutingHelper

    fun handleMessagesDeeplink(uri: Uri, fragmentManager: FragmentManager) {
        val senderId = uri.toString().toLong()
        val messagesFragment = MessagesFragment.newInstance(senderId)
        routingHelper.goToScreen(fragmentManager, messagesFragment)
    }
}

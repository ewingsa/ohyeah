package com.ewingsa.ohyeah.conversations

import androidx.annotation.VisibleForTesting
import com.ewingsa.ohyeah.info.InfoFragment
import com.ewingsa.ohyeah.messages.MessagesFragment
import com.ewingsa.ohyeah.routing.RoutingHelper
import com.ewingsa.ohyeah.setreminder.SetReminderFragment
import javax.inject.Inject

class ConversationsRouter @Inject constructor(
    private val fragment: ConversationsFragment
) : ConversationsContract.Router {

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    var routingHelper = RoutingHelper

    override fun goToInfoScreen() {
        routingHelper.goToScreen(fragment, InfoFragment.newInstance())
    }

    override fun goToSetReminderScreen() {
        routingHelper.goToScreen(fragment, SetReminderFragment.newInstance())
    }

    override fun goToMessagesScreen(senderId: Long) {
        routingHelper.goToScreen(fragment, MessagesFragment.newInstance(senderId))
    }
}

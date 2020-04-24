package com.ewingsa.ohyeah.messages

import androidx.annotation.VisibleForTesting
import com.ewingsa.ohyeah.routing.RoutingHelper
import com.ewingsa.ohyeah.setreminder.SetReminderFragment
import javax.inject.Inject

class MessagesRouter @Inject constructor(
    private val fragment: MessagesFragment
) : MessagesContract.Router {

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    var routingHelper = RoutingHelper

    override fun goBack() {
        routingHelper.goBack(fragment)
    }

    override fun goToSetNewReminder(senderId: Long) {
        routingHelper.goToScreen(fragment, SetReminderFragment.newInstance(senderId, null))
    }

    override fun goToUpdateReminder(messageId: Long) {
        routingHelper.goToScreen(fragment, SetReminderFragment.newInstance(null, messageId))
    }
}

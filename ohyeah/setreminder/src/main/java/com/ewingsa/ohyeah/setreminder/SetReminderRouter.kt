package com.ewingsa.ohyeah.setreminder

import androidx.annotation.VisibleForTesting
import com.ewingsa.ohyeah.routing.RoutingHelper
import javax.inject.Inject

class SetReminderRouter @Inject constructor(
    private val fragment: SetReminderFragment
) : SetReminderContract.Router {

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    var routingHelper = RoutingHelper

    override fun goBack() {
        routingHelper.goBack(fragment)
    }

    override fun goBackToMain() {
        routingHelper.goBack(fragment)
        routingHelper.goBack(fragment)
    }
}

package com.ewingsa.ohyeah.info

import androidx.annotation.VisibleForTesting
import com.ewingsa.ohyeah.routing.RoutingHelper
import javax.inject.Inject

class InfoRouter @Inject constructor(
    private val fragment: InfoFragment
) : InfoContract.Router {

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    var routingHelper = RoutingHelper

    override fun goBack() {
        routingHelper.goBack(fragment)
    }
}

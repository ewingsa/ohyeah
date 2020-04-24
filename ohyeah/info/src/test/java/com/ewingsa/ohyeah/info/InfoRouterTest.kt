package com.ewingsa.ohyeah.info

import com.ewingsa.ohyeah.routing.RoutingHelper
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.Before
import org.junit.Test

class InfoRouterTest {

    private var fragment: InfoFragment = mock()
    private var routingHelper: RoutingHelper = mock()

    private lateinit var infoRouter: InfoRouter

    @Before
    fun setup() {
        infoRouter = InfoRouter(fragment)
        infoRouter.routingHelper = routingHelper
    }

    @Test
    fun testGoBack() {
        infoRouter.goBack()

        verify(routingHelper).goBack(fragment)
    }
}

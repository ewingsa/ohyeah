package com.ewingsa.ohyeah.info

import com.ewingsa.ohyeah.routing.RoutingHelper
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

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

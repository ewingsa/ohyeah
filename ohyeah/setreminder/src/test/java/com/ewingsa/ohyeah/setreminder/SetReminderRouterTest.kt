package com.ewingsa.ohyeah.setreminder

import com.ewingsa.ohyeah.routing.RoutingHelper
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify

class SetReminderRouterTest {

    private var fragment: SetReminderFragment = mock()
    private var routingHelper: RoutingHelper = mock()

    private lateinit var setReminderRouter: SetReminderRouter

    @Before
    fun setup() {
        setReminderRouter = SetReminderRouter(fragment)
        setReminderRouter.routingHelper = routingHelper
    }

    @Test
    fun testGoBack() {
        setReminderRouter.goBack()

        verify(routingHelper).goBack(fragment)
    }

    @Test
    fun testGoBackToMain() {
        setReminderRouter.goBackToMain()

        verify(routingHelper, times(2)).goBack(fragment)
    }
}

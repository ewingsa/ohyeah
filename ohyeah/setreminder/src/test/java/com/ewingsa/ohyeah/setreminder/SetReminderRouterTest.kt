package com.ewingsa.ohyeah.setreminder

import com.ewingsa.ohyeah.routing.RoutingHelper
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import org.junit.Before
import org.junit.Test

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

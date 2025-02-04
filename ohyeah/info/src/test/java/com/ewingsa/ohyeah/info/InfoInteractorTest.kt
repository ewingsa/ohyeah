package com.ewingsa.ohyeah.info

import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify

class InfoInteractorTest {

    private var presenter: InfoContract.Presenter = mock()
    private var router: InfoContract.Router = mock()

    private lateinit var infoInteractor: InfoInteractor

    @Before
    fun setup() {
        infoInteractor = InfoInteractor().apply {
            setPresenter(presenter)
            onRouterAttached(router)
        }
    }

    @Test
    fun testGoBack() {
        infoInteractor.goBack()

        verify(router).goBack()
    }

    @Test
    fun testOnRouterDetached() {
        infoInteractor.onRouterDetached()

        infoInteractor.goBack()

        verify(router, never()).goBack()
    }
}

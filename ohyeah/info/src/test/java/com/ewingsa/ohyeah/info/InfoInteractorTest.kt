package com.ewingsa.ohyeah.info

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import org.junit.Before
import org.junit.Test

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

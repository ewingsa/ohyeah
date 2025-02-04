package com.ewingsa.ohyeah.info

import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.times
import org.mockito.kotlin.verify

class InfoPresenterTest {

    private var view: InfoContract.View = mock()
    private var interactor: InfoContract.Interactor = mock()
    private var router: InfoContract.Router = mock()

    private lateinit var infoPresenter: InfoPresenter

    @Before
    fun setup() {
        infoPresenter = InfoPresenter(interactor).apply {
            onViewAttached(view, router)
        }
    }

    @Test
    fun testOnViewAttached() {
        infoPresenter.onViewAttached(view, router)

        verify(interactor, times(2)).onRouterAttached(router)
        verify(view, times(2)).setViewModel(any())
    }

    @Test
    fun testOnBackPress() {
        infoPresenter.onBackPress()

        verify(interactor).goBack()
    }

    @Test
    fun testOnOpenSourceLicensesPress() {
        infoPresenter.onOpenSourceLicensesPress()

        verify(view).showOpenSourceLicenses()
    }

    @Test
    fun testOnViewDetached() {
        infoPresenter.onViewDetached()

        infoPresenter.onOpenSourceLicensesPress()

        verify(view, never()).showOpenSourceLicenses()
    }
}

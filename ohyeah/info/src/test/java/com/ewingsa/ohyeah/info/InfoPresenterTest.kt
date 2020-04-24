package com.ewingsa.ohyeah.info

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import org.junit.Before
import org.junit.Test

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

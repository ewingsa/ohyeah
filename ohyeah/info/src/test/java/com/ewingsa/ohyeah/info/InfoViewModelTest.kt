package com.ewingsa.ohyeah.info

import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class InfoViewModelTest {

    private val interactions: InfoViewModel.Interactions = mock()

    private lateinit var infoViewModel: InfoViewModel

    @Before
    fun setup() {
        infoViewModel = InfoViewModel(interactions)
    }

    @Test
    fun testOnBackPress() {
        infoViewModel.onBackPress.onClick(mock())

        verify(interactions).onBackPress()
    }

    @Test
    fun testOnOpenSourceLicensesPress() {
        infoViewModel.onOpenSourceLicensesPress.onClick(mock())

        verify(interactions).onOpenSourceLicensesPress()
    }
}

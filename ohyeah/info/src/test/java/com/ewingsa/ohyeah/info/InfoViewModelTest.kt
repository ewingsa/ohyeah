package com.ewingsa.ohyeah.info

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.Before
import org.junit.Test

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

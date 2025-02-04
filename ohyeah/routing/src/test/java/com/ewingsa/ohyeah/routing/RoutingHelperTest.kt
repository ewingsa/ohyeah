package com.ewingsa.ohyeah.routing

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import com.ewingsa.ohyeah.resources.R as MainR

class RoutingHelperTest {

    @Test
    fun testGoBack() {
        val fragment: Fragment = mock()
        val fragmentManager: FragmentManager = mock()

        whenever(fragment.parentFragmentManager).thenReturn(fragmentManager)

        RoutingHelper.goBack(fragment)

        verify(fragmentManager).popBackStack()
    }

    @Test
    fun testGoToScreen_fragment() {
        val fragment: Fragment = mock()
        val newFragment: Fragment = mock()
        val fragmentManager: FragmentManager = mock()
        val fragmentTransaction: FragmentTransaction = mock()

        whenever(fragment.parentFragmentManager).thenReturn(fragmentManager)
        whenever(fragmentManager.beginTransaction()).thenReturn(fragmentTransaction)
        whenever(fragmentTransaction.setCustomAnimations(any(), any(), any(), any())).thenReturn(fragmentTransaction)
        whenever(fragmentTransaction.replace(any(), any())).thenReturn(fragmentTransaction)
        whenever(fragmentTransaction.addToBackStack(NEW_FRAGMENT_NAME)).thenReturn(fragmentTransaction)

        RoutingHelper.goToScreen(fragment, newFragment, NEW_FRAGMENT_NAME)

        verify(fragmentTransaction).setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_out_right, R.anim.slide_in_left)
        verify(fragmentTransaction).replace(MainR.id.fragment_container, newFragment)
        verify(fragmentTransaction).addToBackStack(NEW_FRAGMENT_NAME)
        verify(fragmentTransaction).commit()
    }

    @Test
    fun testGoToScreen_fragmentManager() {
        val newFragment: Fragment = mock()
        val fragmentManager: FragmentManager = mock()
        val fragmentTransaction: FragmentTransaction = mock()

        whenever(fragmentManager.beginTransaction()).thenReturn(fragmentTransaction)
        whenever(fragmentTransaction.setCustomAnimations(any(), any(), any(), any())).thenReturn(fragmentTransaction)
        whenever(fragmentTransaction.replace(any(), any())).thenReturn(fragmentTransaction)
        whenever(fragmentTransaction.addToBackStack(NEW_FRAGMENT_NAME)).thenReturn(fragmentTransaction)

        RoutingHelper.goToScreen(fragmentManager, newFragment, NEW_FRAGMENT_NAME)

        verify(fragmentTransaction).setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_out_right, R.anim.slide_in_left)
        verify(fragmentTransaction).replace(MainR.id.fragment_container, newFragment)
        verify(fragmentTransaction).addToBackStack(NEW_FRAGMENT_NAME)
        verify(fragmentTransaction).commit()
    }

    private companion object {
        const val NEW_FRAGMENT_NAME = "name"
    }
}

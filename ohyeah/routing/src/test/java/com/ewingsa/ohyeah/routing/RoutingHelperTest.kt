package com.ewingsa.ohyeah.routing

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Test

class RoutingHelperTest {

    @Test
    fun testGoBack() {
        val fragment: Fragment = mock()
        val fragmentManager: FragmentManager = mock()

        whenever(fragment.fragmentManager).thenReturn(fragmentManager)

        RoutingHelper.goBack(fragment)

        verify(fragmentManager).popBackStack()
    }

    @Test
    fun testGoToScreen_fragment() {
        val fragment: Fragment = mock()
        val newFragment: Fragment = mock()
        val fragmentManager: FragmentManager = mock()
        val fragmentTransaction: FragmentTransaction = mock()

        whenever(fragment.fragmentManager).thenReturn(fragmentManager)
        whenever(fragmentManager.beginTransaction()).thenReturn(fragmentTransaction)
        whenever(fragmentTransaction.setCustomAnimations(any(), any(), any(), any())).thenReturn(fragmentTransaction)
        whenever(fragmentTransaction.replace(any(), any())).thenReturn(fragmentTransaction)
        whenever(fragmentTransaction.addToBackStack(NEW_FRAGMENT_NAME)).thenReturn(fragmentTransaction)

        RoutingHelper.goToScreen(fragment, newFragment, NEW_FRAGMENT_NAME)

        verify(fragmentTransaction).setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_out_right, R.anim.slide_in_left)
        verify(fragmentTransaction).replace(R.id.fragment_container, newFragment)
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
        verify(fragmentTransaction).replace(R.id.fragment_container, newFragment)
        verify(fragmentTransaction).addToBackStack(NEW_FRAGMENT_NAME)
        verify(fragmentTransaction).commit()
    }

    private companion object {
        const val NEW_FRAGMENT_NAME = "name"
    }
}

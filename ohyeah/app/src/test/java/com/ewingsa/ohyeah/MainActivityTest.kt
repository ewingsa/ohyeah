package com.ewingsa.ohyeah

import com.nhaarman.mockitokotlin2.mock
import dagger.android.DispatchingAndroidInjector
import org.junit.Assert.assertEquals
import org.junit.Test

class MainActivityTest {

    @Test
    fun testAndroidInjector() {
        val fragmentInjector: DispatchingAndroidInjector<Any> = mock()

        val mainActivity = MainActivity()
        mainActivity.fragmentInjector = fragmentInjector

        assertEquals(fragmentInjector, mainActivity.androidInjector())
    }
}

package com.ewingsa.ohyeah

import com.nhaarman.mockitokotlin2.mock
import dagger.android.DispatchingAndroidInjector
import org.junit.Assert.assertEquals
import org.junit.Test

class MainApplicationTest {

    @Test
    fun testAndroidInjector() {
        val androidInjector: DispatchingAndroidInjector<Any> = mock()

        val mainApplication = MainApplication()
        mainApplication.androidInjector = androidInjector

        assertEquals(androidInjector, mainApplication.androidInjector())
    }
}

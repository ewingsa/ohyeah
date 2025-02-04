package com.ewingsa.ohyeah

import dagger.android.DispatchingAndroidInjector
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.mock

class MainApplicationTest {

    @Test
    fun testAndroidInjector() {
        val androidInjector: DispatchingAndroidInjector<Any> = mock()

        val mainApplication = MainApplication()
        mainApplication.androidInjector = androidInjector

        assertEquals(androidInjector, mainApplication.androidInjector())
    }
}

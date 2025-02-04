package com.ewingsa.ohyeah.appinjection

import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class InjectionWorkerTest {

    @Test
    fun testOnActivityCreated() {
        val activity: FragmentActivity = mock()
        val supportFragmentManager: FragmentManager = mock()

        whenever(activity.supportFragmentManager).thenReturn(supportFragmentManager)

        InjectionWorker.onActivityCreated(activity, mock())

        verify(supportFragmentManager).registerFragmentLifecycleCallbacks(any(), eq(true))
    }
}

package com.ewingsa.ohyeah.appinjection

import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Test

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

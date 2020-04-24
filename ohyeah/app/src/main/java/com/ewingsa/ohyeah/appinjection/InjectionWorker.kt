package com.ewingsa.ohyeah.appinjection

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import dagger.android.DispatchingAndroidInjector

object InjectionWorker {

    fun onActivityCreated(activity: Activity, dispatchingAndroidInjector: DispatchingAndroidInjector<Any>) {
        (activity as? FragmentActivity)
            ?.supportFragmentManager
                ?.registerFragmentLifecycleCallbacks(
                    object : FragmentManager.FragmentLifecycleCallbacks() {
                        override fun onFragmentPreAttached(fragmentManager: FragmentManager, fragment: Fragment, context: Context) {
                            (fragment as? Injectable)?.let {
                                dispatchingAndroidInjector.maybeInject(fragment)
                            }
                        }
                    },
                    true
                )
        }
}

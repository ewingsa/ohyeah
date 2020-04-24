package com.ewingsa.ohyeah

import android.content.Context
import android.content.Intent.makeRestartActivityTask
import android.net.Uri
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.ewingsa.ohyeah.appinjection.Injectable
import com.ewingsa.ohyeah.appinjection.InjectionWorker
import com.ewingsa.ohyeah.conversations.ConversationsFragment
import com.ewingsa.ohyeah.deeplink.DeeplinkResolver
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class MainActivity @Inject constructor() : AppCompatActivity(), HasAndroidInjector, Injectable {

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Any>

    override fun onCreate(savedInstanceState: Bundle?) {
        if (INITIALIZED) { // Prevents crashes when recreating MainActivity without state retention
            restart(intent?.data)
        }

        AndroidInjection.inject(this)

        super.onCreate(savedInstanceState)

        setContentView(R.layout.content_main)

        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, ConversationsFragment.newInstance())
            .commit()

        intent?.data?.let {
            DeeplinkResolver.handleMessagesDeeplink(it, supportFragmentManager)
        }

        INITIALIZED = true
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        val view = super.onCreateView(name, context, attrs)
        InjectionWorker.onActivityCreated(this, fragmentInjector)
        return view
    }

    override fun androidInjector(): AndroidInjector<Any> {
        return fragmentInjector
    }

    private fun restart(data: Uri?) {
        makeRestartActivityTask(applicationContext?.packageManager?.getLaunchIntentForPackage(packageName)?.component)?.let {
            it.data = data
            applicationContext?.startActivity(it)
        }
        Runtime.getRuntime().exit(0)
    }

    private companion object {
        var INITIALIZED = false
    }
}

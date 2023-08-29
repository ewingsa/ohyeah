package com.ewingsa.ohyeah

import android.Manifest.permission.POST_NOTIFICATIONS
import android.content.Context
import android.content.Intent.makeRestartActivityTask
import android.net.Uri
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.TIRAMISU
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
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
import com.ewingsa.ohyeah.resources.R as MainR

class MainActivity @Inject constructor() : AppCompatActivity(), HasAndroidInjector, Injectable {

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Any>

    override fun onCreate(savedInstanceState: Bundle?) {
        if (INITIALIZED) { // Prevents crashes when recreating MainActivity without state retention
            restart(intent?.data)
        }

        AndroidInjection.inject(this)

        super.onCreate(savedInstanceState)

        setContentView(MainR.layout.content_main)

        supportFragmentManager.beginTransaction()
            .add(MainR.id.fragment_container, ConversationsFragment.newInstance())
            .commit()

        intent?.data?.let {
            DeeplinkResolver.handleMessagesDeeplink(it, supportFragmentManager)
        }

        if (SDK_INT >= TIRAMISU) registerForActivityResult(RequestPermission()) {}.launch(POST_NOTIFICATIONS)

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

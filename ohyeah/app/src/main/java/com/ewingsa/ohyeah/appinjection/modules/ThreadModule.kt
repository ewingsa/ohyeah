package com.ewingsa.ohyeah.appinjection.modules

import com.ewingsa.ohyeah.appinjection.qualifiers.IoThreadScheduler
import com.ewingsa.ohyeah.appinjection.qualifiers.MainThreadScheduler
import com.ewingsa.ohyeah.appinjection.scopes.PerApplication
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@Module
abstract class ThreadModule {

    companion object {

        @PerApplication
        @MainThreadScheduler
        @Provides
        @JvmStatic
        fun provideMainThreadScheduler(): Scheduler = AndroidSchedulers.mainThread()

        @PerApplication
        @IoThreadScheduler
        @Provides
        @JvmStatic
        fun provideIoThreadScheduler(): Scheduler = Schedulers.io()
    }
}

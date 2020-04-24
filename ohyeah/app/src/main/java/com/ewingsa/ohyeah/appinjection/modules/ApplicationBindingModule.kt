package com.ewingsa.ohyeah.appinjection.modules

import android.app.Application
import android.content.res.Resources
import com.ewingsa.ohyeah.MainApplication
import com.ewingsa.ohyeah.appinjection.scopes.PerApplication
import com.ewingsa.ohyeah.database.DatabaseModule
import com.ewingsa.ohyeah.receiver.ReceiverModule
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module(includes = [ActivityBindingModule::class, ReceiverModule::class, DatabaseModule::class, ThreadModule::class])
abstract class ApplicationBindingModule {

    @Binds
    abstract fun bindApplication(mainApplication: MainApplication): Application

    companion object {

        @PerApplication
        @Provides
        @JvmStatic
        fun provideResources(mainApplication: MainApplication): Resources = mainApplication.resources
    }
}

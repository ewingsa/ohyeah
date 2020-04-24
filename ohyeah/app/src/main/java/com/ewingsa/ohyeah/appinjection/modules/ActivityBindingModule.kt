package com.ewingsa.ohyeah.appinjection.modules

import com.ewingsa.ohyeah.MainActivity
import com.ewingsa.ohyeah.appinjection.scopes.PerActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingModule {

    @PerActivity
    @ContributesAndroidInjector(modules = [FragmentBindingModule::class])
    abstract fun contributeActivityAndroidInjector(): MainActivity
}

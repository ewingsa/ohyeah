package com.ewingsa.ohyeah.receiver

import dagger.Module
import dagger.android.ContributesAndroidInjector
import javax.inject.Singleton

@Module
abstract class ReceiverModule {

    @Singleton
    @ContributesAndroidInjector
    abstract fun contributeAlarmReceiver(): AlarmReceiver
}

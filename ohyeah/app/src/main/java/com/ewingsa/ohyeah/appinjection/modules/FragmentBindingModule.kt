package com.ewingsa.ohyeah.appinjection.modules

import com.ewingsa.ohyeah.appinjection.scopes.PerFragment
import com.ewingsa.ohyeah.conversations.ConversationsFragment
import com.ewingsa.ohyeah.conversations.ConversationsModule
import com.ewingsa.ohyeah.info.InfoFragment
import com.ewingsa.ohyeah.info.InfoModule
import com.ewingsa.ohyeah.messages.MessagesFragment
import com.ewingsa.ohyeah.messages.MessagesModule
import com.ewingsa.ohyeah.setreminder.SetReminderFragment
import com.ewingsa.ohyeah.setreminder.SetReminderModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBindingModule {

    @PerFragment
    @ContributesAndroidInjector(modules = [ConversationsModule::class])
    abstract fun provideConversationsFragment(): ConversationsFragment

    @PerFragment
    @ContributesAndroidInjector(modules = [InfoModule::class])
    abstract fun provideInfoFragment(): InfoFragment

    @PerFragment
    @ContributesAndroidInjector(modules = [MessagesModule::class])
    abstract fun provideMessagesFragment(): MessagesFragment

    @PerFragment
    @ContributesAndroidInjector(modules = [SetReminderModule::class])
    abstract fun provideSetReminderFragment(): SetReminderFragment
}

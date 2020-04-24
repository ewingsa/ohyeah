package com.ewingsa.ohyeah.setreminder

import dagger.Binds
import dagger.Module

@Module
abstract class SetReminderModule {

    @Binds
    abstract fun bindSetReminderPresenter(setReminderPresenter: SetReminderPresenter): SetReminderContract.Presenter

    @Binds
    abstract fun bindSetReminderInteractor(setReminderInteractor: SetReminderInteractor): SetReminderContract.Interactor

    @Binds
    abstract fun bindSetReminderRepository(setReminderRepository: SetReminderRepository): SetReminderContract.Repository

    @Binds
    abstract fun bindSetReminderRouter(setReminderRouter: SetReminderRouter): SetReminderContract.Router
}

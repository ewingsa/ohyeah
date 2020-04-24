package com.ewingsa.ohyeah.info

import dagger.Binds
import dagger.Module

@Module
abstract class InfoModule {

    @Binds
    abstract fun bindInfoPresenter(infoPresenter: InfoPresenter): InfoContract.Presenter

    @Binds
    abstract fun bindInfoInteractor(infoInteractor: InfoInteractor): InfoContract.Interactor

    @Binds
    abstract fun bindInfoRouter(infoRouter: InfoRouter): InfoContract.Router
}

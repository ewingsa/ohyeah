package com.ewingsa.ohyeah.messages

import dagger.Binds
import dagger.Module

@Module
abstract class MessagesModule {

    @Binds
    abstract fun bindMessagesPresenter(messagesPresenter: MessagesPresenter): MessagesContract.Presenter

    @Binds
    abstract fun bindMessagesInteractor(messagesInteractor: MessagesInteractor): MessagesContract.Interactor

    @Binds
    abstract fun bindMessagesRepository(messagesRepository: MessagesRepository): MessagesContract.Repository

    @Binds
    abstract fun bindMessagesRouter(messagesRouter: MessagesRouter): MessagesContract.Router
}

package com.ewingsa.ohyeah.conversations

import dagger.Binds
import dagger.Module

@Module
abstract class ConversationsModule {

    @Binds
    abstract fun bindConversationsPresenter(conversationsPresenter: ConversationsPresenter): ConversationsContract.Presenter

    @Binds
    abstract fun bindConversationsInteractor(conversationsInteractor: ConversationsInteractor): ConversationsContract.Interactor

    @Binds
    abstract fun bindConversationsRepository(conversationsRepository: ConversationsRepository): ConversationsContract.Repository

    @Binds
    abstract fun bindConversationsRouter(conversationsRouter: ConversationsRouter): ConversationsContract.Router
}

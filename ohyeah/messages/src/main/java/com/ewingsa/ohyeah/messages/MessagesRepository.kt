package com.ewingsa.ohyeah.messages

import com.ewingsa.ohyeah.appinjection.qualifiers.IoThreadScheduler
import com.ewingsa.ohyeah.appinjection.qualifiers.MainThreadScheduler
import com.ewingsa.ohyeah.database.MessageDao
import com.ewingsa.ohyeah.database.SenderMessage
import com.ewingsa.ohyeah.viper.ViperContract
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject

class MessagesRepository @Inject constructor(
    private val messageDao: MessageDao,
    @MainThreadScheduler private val mainThreadScheduler: Scheduler,
    @IoThreadScheduler private val ioThreadScheduler: Scheduler
) : MessagesContract.Repository {

    private var interactor: MessagesContract.Interactor? = null

    private val compositeDisposable = CompositeDisposable()

    override fun setInteractor(interactor: ViperContract.Interactor) {
        this.interactor = interactor as? MessagesContract.Interactor
    }

    override fun fetchMessages(senderId: Long) {
        Observable.zip(
            messageDao.getExistingConversation(senderId).toObservable(),
            messageDao.findMessages(senderId)
        ) { sender, messages ->
            messages.map { SenderMessage(sender, it) }
        }
            .subscribeOn(ioThreadScheduler)
            .observeOn(mainThreadScheduler)
            .subscribe(
                { messages -> interactor?.onMessagesRetrieved(messages) },
                {}
            )
            .addTo(compositeDisposable)
    }

    override fun markReadMessages(senderId: Long, timestamp: Long) {
        messageDao.markReadMessages(senderId, timestamp)
            .subscribeOn(ioThreadScheduler)
            .observeOn(mainThreadScheduler)
            .subscribe(
                {},
                {}
            )
            .addTo(compositeDisposable)
    }

    override fun clear() {
        compositeDisposable.clear()
    }
}

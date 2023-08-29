package com.ewingsa.ohyeah.conversations

import com.ewingsa.ohyeah.appinjection.qualifiers.IoThreadScheduler
import com.ewingsa.ohyeah.appinjection.qualifiers.MainThreadScheduler
import com.ewingsa.ohyeah.database.MessageDao
import com.ewingsa.ohyeah.viper.ViperContract
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject

class ConversationsRepository @Inject constructor(
    private val messageDao: MessageDao,
    @MainThreadScheduler private val mainThreadScheduler: Scheduler,
    @IoThreadScheduler private val ioThreadScheduler: Scheduler
) : ConversationsContract.Repository {

    private var interactor: ConversationsContract.Interactor? = null

    private val compositeDisposable = CompositeDisposable()

    override fun setInteractor(interactor: ViperContract.Interactor) {
        this.interactor = interactor as? ConversationsContract.Interactor
    }

    /**
     * Queries all conversations, ordered by the most recent message before the timestamp.
     * Conversation with only a scheduled message or messages will be listed last,
     * ordered by the most soon-to-come message after the timestamp.
     */
    override fun fetchConversations(timestamp: Long) {
        Observable.zip(
            messageDao.getPreviousConversations(timestamp),
            messageDao.getFutureConversations(timestamp)
        ) { previous, future ->
            (previous + future).distinctBy { it.sender.senderId }
        }
            .subscribeOn(ioThreadScheduler)
            .observeOn(mainThreadScheduler)
            .subscribe(
                { conversations -> interactor?.onConversationsRetrieved(conversations) },
                {}
            )
            .addTo(compositeDisposable)
    }

    override fun fetchMessages(senderId: Long) {
        messageDao.findMessages(senderId)
            .subscribeOn(ioThreadScheduler)
            .observeOn(mainThreadScheduler)
            .subscribe(
                { messages -> interactor?.onMessagesRetrieved(messages) },
                {}
            )
            .addTo(compositeDisposable)
    }

    override fun deleteConversation(senderId: Long) {
        messageDao.deleteMessages(senderId).andThen(messageDao.deleteSender(senderId))
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

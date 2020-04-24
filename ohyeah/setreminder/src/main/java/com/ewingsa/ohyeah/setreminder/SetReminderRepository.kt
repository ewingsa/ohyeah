package com.ewingsa.ohyeah.setreminder

import com.ewingsa.ohyeah.appinjection.qualifiers.IoThreadScheduler
import com.ewingsa.ohyeah.appinjection.qualifiers.MainThreadScheduler
import com.ewingsa.ohyeah.database.Message
import com.ewingsa.ohyeah.database.MessageDao
import com.ewingsa.ohyeah.database.Sender
import com.ewingsa.ohyeah.viper.ViperContract
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject

class SetReminderRepository @Inject constructor(
    private val messageDao: MessageDao,
    @MainThreadScheduler private val mainThreadScheduler: Scheduler,
    @IoThreadScheduler private val ioThreadScheduler: Scheduler
) : SetReminderContract.Repository {

    private var interactor: SetReminderContract.Interactor? = null

    private val compositeDisposable = CompositeDisposable()

    override fun setInteractor(interactor: ViperContract.Interactor) {
        this.interactor = interactor as? SetReminderContract.Interactor
    }

    override fun getExistingReminder(messageId: Long) {
        messageDao.findMessage(messageId)
            .subscribeOn(ioThreadScheduler)
            .observeOn(mainThreadScheduler)
            .subscribe(
                { message -> getExistingReminderSender(message) },
                {}
            )
            .addTo(compositeDisposable)
    }

    private fun getExistingReminderSender(message: Message) {
        messageDao.getExistingConversation(message.senderId)
            .subscribeOn(ioThreadScheduler)
            .observeOn(mainThreadScheduler)
            .subscribe(
                { sender -> interactor?.onReminderLoaded(message, sender) },
                {}
            )
            .addTo(compositeDisposable)
    }

    override fun getExistingSender(senderId: Long) {
        messageDao.getExistingConversation(senderId)
            .subscribeOn(ioThreadScheduler)
            .observeOn(mainThreadScheduler)
            .subscribe(
                { sender -> interactor?.onSenderLoaded(sender) },
                {}
            )
            .addTo(compositeDisposable)
    }

    override fun getExistingConversation(name: String) {
        messageDao.getExistingConversation(name)
            .subscribeOn(ioThreadScheduler)
            .observeOn(mainThreadScheduler)
            .defaultIfEmpty(Sender(-1L, "", null))
            .subscribe(
                { sender ->
                    if (sender.senderId == -1L) {
                        interactor?.onNewSender()
                    } else {
                        interactor?.onSenderSaved(sender.senderId)
                    }
                },
                {}
            )
            .addTo(compositeDisposable)
    }

    override fun saveNewSender(sender: Sender) {
        Single.fromCallable { messageDao.insertConversation(sender) }
            .subscribeOn(ioThreadScheduler)
            .observeOn(mainThreadScheduler)
            .subscribe(
                { senderId -> interactor?.onSenderSaved(senderId) },
                {}
            )
            .addTo(compositeDisposable)
    }

    override fun saveNewMessage(message: Message) {
        Single.fromCallable { messageDao.insertMessage(message) }
            .subscribeOn(ioThreadScheduler)
            .observeOn(mainThreadScheduler)
            .subscribe(
                { messageId -> interactor?.onMessageSaved(messageId) },
                {}
            )
            .addTo(compositeDisposable)
    }

    override fun updateMessage(message: Message) {
        messageDao.updateMessage(message)
            .subscribeOn(ioThreadScheduler)
            .observeOn(mainThreadScheduler)
            .subscribe(
                { interactor?.onMessageUpdated() },
                {}
            )
            .addTo(compositeDisposable)
    }

    override fun updateSenderName(senderId: Long, name: String) {
        messageDao.updateConversationName(senderId, name)
            .subscribeOn(ioThreadScheduler)
            .observeOn(mainThreadScheduler)
            .subscribe(
                {},
                {}
            )
            .addTo(compositeDisposable)
    }

    override fun updateSenderPicture(senderId: Long, photoUri: String) {
        messageDao.updateConversationPhoto(senderId, photoUri)
            .subscribeOn(ioThreadScheduler)
            .observeOn(mainThreadScheduler)
            .subscribe(
                {},
                {}
            )
            .addTo(compositeDisposable)
    }

    override fun deleteReminder(messageId: Long) {
        messageDao.deleteMessage(messageId)
            .subscribeOn(ioThreadScheduler)
            .observeOn(mainThreadScheduler)
            .subscribe(
                { interactor?.onReminderDeleted() },
                {}
            )
            .addTo(compositeDisposable)
    }

    override fun isSenderOrphaned(senderId: Long) {
        messageDao.isConversationEmpty(senderId)
            .subscribeOn(ioThreadScheduler)
            .observeOn(mainThreadScheduler)
            .subscribe(
                { isOrphaned -> interactor?.onSenderOrphaned(isOrphaned) },
                {}
            )
            .addTo(compositeDisposable)
    }

    override fun deleteSender(senderId: Long) {
        messageDao.deleteSender(senderId)
            .subscribeOn(ioThreadScheduler)
            .observeOn(mainThreadScheduler)
            .subscribe(
                { interactor?.onSenderDeleted() },
                {}
            )
            .addTo(compositeDisposable)
    }

    override fun clear() {
        compositeDisposable.clear()
    }
}

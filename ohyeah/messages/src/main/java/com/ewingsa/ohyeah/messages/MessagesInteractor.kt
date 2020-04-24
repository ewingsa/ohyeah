package com.ewingsa.ohyeah.messages

import androidx.annotation.VisibleForTesting
import com.ewingsa.ohyeah.database.SenderMessage
import com.ewingsa.ohyeah.helpers.DateHelper
import com.ewingsa.ohyeah.helpers.DrawableHelper
import com.ewingsa.ohyeah.messages.datamodels.DateLabelDataModel
import com.ewingsa.ohyeah.messages.datamodels.MessageDataModel
import com.ewingsa.ohyeah.messages.datamodels.MessageItem
import com.ewingsa.ohyeah.viper.ViperContract
import javax.inject.Inject

class MessagesInteractor @Inject constructor(
    private val repository: MessagesContract.Repository
) : MessagesContract.Interactor {

    private var presenter: MessagesContract.Presenter? = null

    private var router: MessagesContract.Router? = null

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    var drawableHelper = DrawableHelper
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    var dateHelper = DateHelper

    init {
        repository.setInteractor(this)
    }

    override fun setPresenter(presenter: ViperContract.Presenter) {
        this.presenter = presenter as? MessagesContract.Presenter
    }

    override fun onRouterAttached(router: ViperContract.Router?) {
        this.router = router as? MessagesContract.Router?
    }

    override fun goBack() {
        router?.goBack()
    }

    override fun onNewMessage(senderId: Long) {
        router?.goToSetNewReminder(senderId)
    }

    override fun fetchMessages(senderId: Long?) {
        repository.fetchMessages(senderId ?: 0L)
    }

    // Builds a list of messages and dates from the the bottom to the top,
    // then scrolls to the position of the most recent message.
    override fun onMessagesRetrieved(messages: List<SenderMessage>) {
        messages.firstOrNull()?.let { firstMessage ->
            presenter?.setSenderName(firstMessage.sender.name)

            val photoUri = messages.firstOrNull()?.sender?.photoUri
            val bitmap = drawableHelper.getSmallBitmap(photoUri, presenter?.getContext())

            val dataModels = mutableListOf<MessageItem>()

            var uniqueDates = 0
            var mostRecentDateProcessed = dateHelper.formatDate(firstMessage.message.timestamp)
            var mostRecentMessageIndex = 0
            var mostRecentMessageTimestamp = -1L
            val now = dateHelper.now()

            var index = 0

            while (index < messages.size) {
                val currentMessage = messages[index]
                val messageDate = dateHelper.formatDate(currentMessage.message.timestamp)

                if (messageDate != mostRecentDateProcessed) {
                    dataModels.add(DateLabelDataModel(mostRecentDateProcessed))
                    mostRecentDateProcessed = messageDate
                    uniqueDates++
                } else {
                    if (currentMessage.message.timestamp in (mostRecentMessageTimestamp..now)) {
                        mostRecentMessageIndex = index + uniqueDates
                        mostRecentMessageTimestamp = currentMessage.message.timestamp
                    }
                    dataModels.add(
                        MessageDataModel(
                            currentMessage.message.messageId,
                            currentMessage.message.message,
                            dateHelper.formatTime(currentMessage.message.timestamp),
                            bitmap)
                    )
                    index++
                }
            }

            dataModels.add(DateLabelDataModel(mostRecentDateProcessed))

            presenter?.addMessages(dataModels)

            if (mostRecentMessageIndex != 0) {
                presenter?.scrollToPosition(mostRecentMessageIndex)
            }

            repository.markReadMessages(firstMessage.sender.senderId, now)
        } ?: router?.goBack()
    }

    override fun onEditMessage(messageId: Long) {
        router?.goToUpdateReminder(messageId)
    }

    override fun onRouterDetached() {
        router = null
    }

    override fun clear() {
        repository.clear()
    }
}

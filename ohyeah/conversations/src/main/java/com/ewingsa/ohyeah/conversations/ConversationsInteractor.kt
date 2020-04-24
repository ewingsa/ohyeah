package com.ewingsa.ohyeah.conversations

import android.content.res.Resources
import androidx.annotation.VisibleForTesting
import com.ewingsa.ohyeah.conversations.datamodels.ConversationDataModel
import com.ewingsa.ohyeah.conversations.datamodels.ConversationItem
import com.ewingsa.ohyeah.conversations.datamodels.UpcomingLabelDataModel
import com.ewingsa.ohyeah.database.Message
import com.ewingsa.ohyeah.database.PreviewSenderMessage
import com.ewingsa.ohyeah.helpers.DateHelper
import com.ewingsa.ohyeah.helpers.DrawableHelper
import com.ewingsa.ohyeah.helpers.IntentHelper
import com.ewingsa.ohyeah.receiver.AlarmReceiver
import com.ewingsa.ohyeah.viper.ViperContract
import javax.inject.Inject

class ConversationsInteractor @Inject constructor(
    private val repository: ConversationsContract.Repository,
    private val resources: Resources
) : ConversationsContract.Interactor {

    private var presenter: ConversationsContract.Presenter? = null

    private var router: ConversationsContract.Router? = null

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    var dateHelper = DateHelper
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    var intentHelper = IntentHelper

    init {
        repository.setInteractor(this)
    }

    override fun setPresenter(presenter: ViperContract.Presenter) {
        this.presenter = presenter as? ConversationsContract.Presenter
    }

    override fun onRouterAttached(router: ViperContract.Router?) {
        this.router = router as? ConversationsContract.Router?
    }

    override fun goToInfo() {
        router?.goToInfoScreen()
    }

    override fun goToSetReminder() {
        router?.goToSetReminderScreen()
    }

    override fun fetchConversations() {
        repository.fetchConversations(System.currentTimeMillis())
    }

    // Displays most recent conversations first.
    // Conversations with only future messages are listed under an upcoming label
    override fun onConversationsRetrieved(conversations: List<PreviewSenderMessage>) {
        if (conversations.isEmpty()) {
            presenter?.addNoConversationsMessage()
        }

        val dataModels = mutableListOf<ConversationItem>()

        val now = dateHelper.now()
        var upcomingLabelAdded = false

        var index = 0
        while (index < conversations.size) {
            val conversation = conversations[index]

            if (conversation.message.timestamp > now && !upcomingLabelAdded && dataModels.size > 0) {
                dataModels.add(UpcomingLabelDataModel())
                upcomingLabelAdded = true
            }

            dataModels.add(
                ConversationDataModel(
                    conversation.sender.senderId,
                    conversation.sender.name,
                    conversation.message.message,
                    DateHelper.formatPreviewTime(conversation.message.timestamp),
                    DrawableHelper.getSmallBitmap(conversation.sender.photoUri, presenter?.getContext()),
                    getTrimmedNumberUnread(conversation.unread)
                )
            )
            index++
        }
        presenter?.addConversations(dataModels)
    }

    private fun getTrimmedNumberUnread(unread: Int?): String? {
        unread?.let {
            if (it in 1..9) {
                return it.toString()
            }
            if (it > 9) {
                return resources.getString(R.string.conversation_nine_plus)
            }
        }
        return null
    }

    override fun goToConversation(senderId: Long) {
        router?.goToMessagesScreen(senderId)
    }

    override fun deleteConversation(senderId: Long) {
        repository.fetchMessages(senderId)
    }

    override fun onMessagesRetrieved(messages: List<Message>) {
        messages.firstOrNull()?.let {
            repository.deleteConversation(it.senderId)
        }
        presenter?.getContext()?.let {
            for (message in messages) {
                intentHelper.deletePendingIntent(it, AlarmReceiver::class.java, message.messageId)
            }
        }
    }

    override fun onRouterDetached() {
        router = null
    }

    override fun clear() {
        repository.clear()
    }
}

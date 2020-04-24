package com.ewingsa.ohyeah.conversations

import android.content.Context
import com.ewingsa.ohyeah.conversations.datamodels.ConversationItem
import com.ewingsa.ohyeah.conversations.viewmodels.ConversationViewModel
import com.ewingsa.ohyeah.conversations.viewmodels.ConversationsScreenViewModel
import com.ewingsa.ohyeah.database.Message
import com.ewingsa.ohyeah.database.PreviewSenderMessage
import com.ewingsa.ohyeah.viper.ViperContract

interface ConversationsContract {

    interface View : ViperContract.View {
        fun setScreenViewModel(screenViewModel: ConversationsScreenViewModel)
        fun addNoConversationsMessage()
        fun getContext(): Context?
        fun addConversations(conversations: List<ConversationViewModel>)
    }

    interface Presenter : ViperContract.Presenter {
        fun addNoConversationsMessage()
        fun getContext(): Context?
        fun addConversations(conversations: List<ConversationItem>)
    }

    interface Interactor : ViperContract.Interactor {
        fun goToInfo()
        fun goToSetReminder()
        fun fetchConversations()
        fun onConversationsRetrieved(conversations: List<PreviewSenderMessage>)
        fun goToConversation(senderId: Long)
        fun deleteConversation(senderId: Long)
        fun onMessagesRetrieved(messages: List<Message>)
        fun clear()
    }

    interface Repository : ViperContract.Repository {
        fun fetchConversations(timestamp: Long)
        fun fetchMessages(senderId: Long)
        fun deleteConversation(senderId: Long)
        fun clear()
    }

    interface Router : ViperContract.Router {
        fun goToInfoScreen()
        fun goToSetReminderScreen()
        fun goToMessagesScreen(senderId: Long)
    }
}

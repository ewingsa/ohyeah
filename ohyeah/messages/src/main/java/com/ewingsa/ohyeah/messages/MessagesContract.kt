package com.ewingsa.ohyeah.messages

import android.content.Context
import com.ewingsa.ohyeah.database.SenderMessage
import com.ewingsa.ohyeah.messages.datamodels.MessageItem
import com.ewingsa.ohyeah.messages.viewmodels.MessageViewModel
import com.ewingsa.ohyeah.messages.viewmodels.MessagesToolbarViewModel
import com.ewingsa.ohyeah.viper.ViperContract

interface MessagesContract {

    interface View : ViperContract.View {
        fun getSenderId(): Long?
        fun setToolbarViewModel(toolbarViewModel: MessagesToolbarViewModel)
        fun setSenderName(title: String)
        fun getContext(): Context?
        fun addMessages(messages: List<MessageViewModel>)
        fun getMessageCount(): Int?
        fun scrollToPosition(position: Int)
    }

    interface Presenter : ViperContract.Presenter {
        fun setSenderName(name: String)
        fun getContext(): Context?
        fun addMessages(messages: List<MessageItem>)
        fun scrollToPosition(position: Int)
    }

    interface Interactor : ViperContract.Interactor {
        fun goBack()
        fun onNewMessage(senderId: Long)
        fun fetchMessages(senderId: Long?)
        fun onMessagesRetrieved(messages: List<SenderMessage>)
        fun onEditMessage(messageId: Long)
        fun clear()
    }

    interface Repository : ViperContract.Repository {
        fun fetchMessages(senderId: Long)
        fun markReadMessages(senderId: Long, timestamp: Long)
        fun clear()
    }

    interface Router : ViperContract.Router {
        fun goBack()
        fun goToSetNewReminder(senderId: Long)
        fun goToUpdateReminder(messageId: Long)
    }
}

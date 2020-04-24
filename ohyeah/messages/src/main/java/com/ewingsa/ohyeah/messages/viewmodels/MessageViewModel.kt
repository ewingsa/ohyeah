package com.ewingsa.ohyeah.messages.viewmodels

import com.ewingsa.ohyeah.messages.datamodels.MessageDataModel
import com.ewingsa.ohyeah.messages.datamodels.MessageItem

class MessageViewModel(val messageDataModel: MessageItem, private val interactions: Interactions) {

    val onLongPress = {
        (messageDataModel as? MessageDataModel)?.let {
            interactions.onEditMessageRequested(it.id)
        }
    }

    interface Interactions {
        fun onEditMessageRequested(messageId: Long)
    }
}

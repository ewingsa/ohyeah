package com.ewingsa.ohyeah.conversations.viewmodels

import com.ewingsa.ohyeah.conversations.datamodels.ConversationDataModel
import com.ewingsa.ohyeah.conversations.datamodels.ConversationItem

class ConversationViewModel(val conversationDataModel: ConversationItem, private val interactions: Interactions) {

    val onPress = {
        (conversationDataModel as? ConversationDataModel)?.let {
            interactions.onConversationSelected(it.senderId)
        }
    }

    val onLongPress = {
        (conversationDataModel as? ConversationDataModel)?.let {
            interactions.onDeleteConversationRequested(it.senderId)
        }
    }

    interface Interactions {
        fun onConversationSelected(senderId: Long)
        fun onDeleteConversationRequested(senderId: Long)
    }
}

package com.ewingsa.ohyeah.conversations.viewmodels

import android.view.View

class ConversationsScreenViewModel(private val interactions: Interactions) {

    val onInfoPress = View.OnClickListener { interactions.onInfoPress() }

    val onNewPress = View.OnClickListener { interactions.onNewPress() }

    interface Interactions {
        fun onInfoPress()
        fun onNewPress()
    }
}

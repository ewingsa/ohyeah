package com.ewingsa.ohyeah.messages.viewmodels

import android.view.View

class MessagesToolbarViewModel(private val interactions: Interactions) {

    val onBackPress = View.OnClickListener { interactions.onBackPress() }

    val onNewPress = View.OnClickListener { interactions.onNewPress() }

    interface Interactions {
        fun onBackPress()
        fun onNewPress()
    }
}

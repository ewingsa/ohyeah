package com.ewingsa.ohyeah.info

import android.view.View

class InfoViewModel(private val interactions: Interactions) {

    val onBackPress = View.OnClickListener { interactions.onBackPress() }

    val onOpenSourceLicensesPress = View.OnClickListener { interactions.onOpenSourceLicensesPress() }

    interface Interactions {
        fun onBackPress()
        fun onOpenSourceLicensesPress()
    }
}

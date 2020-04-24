package com.ewingsa.ohyeah.info

import com.ewingsa.ohyeah.viper.ViperContract

interface InfoContract {

    interface View : ViperContract.View {
        fun setViewModel(infoViewModel: InfoViewModel)
        fun showOpenSourceLicenses()
    }

    interface Presenter : ViperContract.Presenter

    interface Interactor : ViperContract.Interactor {
        fun goBack()
    }

    interface Router : ViperContract.Router {
        fun goBack()
    }
}

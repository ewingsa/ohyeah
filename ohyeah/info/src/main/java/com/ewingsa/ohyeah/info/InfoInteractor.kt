package com.ewingsa.ohyeah.info

import com.ewingsa.ohyeah.viper.ViperContract
import javax.inject.Inject

class InfoInteractor @Inject constructor() : InfoContract.Interactor {

    private var presenter: InfoContract.Presenter? = null

    private var router: InfoContract.Router? = null

    override fun setPresenter(presenter: ViperContract.Presenter) {
        this.presenter = presenter as? InfoContract.Presenter
    }

    override fun onRouterAttached(router: ViperContract.Router?) {
        this.router = router as? InfoContract.Router?
    }

    override fun goBack() {
        router?.goBack()
    }

    override fun onRouterDetached() {
        router = null
    }
}

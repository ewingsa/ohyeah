package com.ewingsa.ohyeah.info

import com.ewingsa.ohyeah.viper.ViperContract
import javax.inject.Inject

class InfoPresenter @Inject constructor(
    private val interactor: InfoContract.Interactor
) : InfoContract.Presenter, InfoViewModel.Interactions {

    private var view: InfoContract.View? = null

    init {
        interactor.setPresenter(this)
    }

    override fun onViewAttached(view: ViperContract.View, router: ViperContract.Router?) {
        this.view = view as? InfoContract.View?

        interactor.onRouterAttached(router)

        this.view?.setViewModel(InfoViewModel(this))
    }

    override fun onBackPress() {
        interactor.goBack()
    }

    override fun onOpenSourceLicensesPress() {
        view?.showOpenSourceLicenses()
    }

    override fun onViewDetached() {
        view = null
    }
}

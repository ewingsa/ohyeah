package com.ewingsa.ohyeah.viper

interface ViperContract {

    interface View

    interface Presenter {
        fun onViewAttached(view: View, router: Router?)

        fun onViewDetached()
    }

    interface Interactor {
        fun setPresenter(presenter: Presenter)

        fun onRouterAttached(router: Router?)

        fun onRouterDetached()
    }

    interface Repository {
        fun setInteractor(interactor: Interactor)
    }

    interface Router
}

package com.ewingsa.ohyeah.viper

import androidx.fragment.app.Fragment
import javax.inject.Inject

open class BaseViperFragment<P : ViperContract.Presenter, R : ViperContract.Router> : Fragment(), ViperContract.View {

    @Inject
    lateinit var presenter: P
    @Inject
    lateinit var router: R

    override fun onStart() {
        super.onStart()

        presenter.onViewAttached(this, router)
    }

    override fun onStop() {
        presenter.onViewDetached()
        super.onStop()
    }
}

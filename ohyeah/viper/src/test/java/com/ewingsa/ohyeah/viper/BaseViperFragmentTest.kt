package com.ewingsa.ohyeah.viper

import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class BaseViperFragmentTest {

    private var presenter: ViperContract.Presenter = mock()
    private var router: ViperContract.Router = mock()

    private lateinit var baseViperFragment: BaseViperFragment<ViperContract.Presenter, ViperContract.Router>

    @Before
    fun setup() {
        baseViperFragment = BaseViperFragment()
        baseViperFragment.presenter = presenter
        baseViperFragment.router = router
    }

    @Test
    fun testOnStart() {
        baseViperFragment.onStart()

        verify(presenter).onViewAttached(baseViperFragment, router)
    }

    @Test
    fun testOnStop() {
        baseViperFragment.onStop()

        verify(presenter).onViewDetached()
    }
}

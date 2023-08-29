package com.ewingsa.ohyeah.routing

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.ewingsa.ohyeah.resources.R as MainR

object RoutingHelper {

    fun goBack(fragment: Fragment) {
        fragment.parentFragmentManager.popBackStack()
    }

    fun goToScreen(oldFragment: Fragment, newFragment: Fragment, name: String? = null) {
        goToScreen(oldFragment.parentFragmentManager, newFragment, name)
    }

    fun goToScreen(fragmentManager: FragmentManager?, newFragment: Fragment, name: String? = null) {
        fragmentManager?.beginTransaction()
            ?.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_out_right, R.anim.slide_in_left)
            ?.replace(MainR.id.fragment_container, newFragment)
            ?.addToBackStack(name)
            ?.commit()
    }
}

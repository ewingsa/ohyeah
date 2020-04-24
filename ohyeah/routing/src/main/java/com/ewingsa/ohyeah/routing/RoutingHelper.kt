package com.ewingsa.ohyeah.routing

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

object RoutingHelper {

    fun goBack(fragment: Fragment) {
        fragment.fragmentManager?.popBackStack()
    }

    fun goToScreen(oldFragment: Fragment, newFragment: Fragment, name: String? = null) {
        goToScreen(oldFragment.fragmentManager, newFragment, name)
    }

    fun goToScreen(fragmentManager: FragmentManager?, newFragment: Fragment, name: String? = null) {
        fragmentManager?.beginTransaction()
            ?.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_out_right, R.anim.slide_in_left)
            ?.replace(R.id.fragment_container, newFragment)
            ?.addToBackStack(name)
            ?.commit()
    }
}

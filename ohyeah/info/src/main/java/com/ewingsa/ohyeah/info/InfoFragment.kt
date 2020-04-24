package com.ewingsa.ohyeah.info

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.webkit.WebView
import android.widget.ImageView
import android.widget.LinearLayout.LayoutParams
import androidx.annotation.VisibleForTesting
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.viewpager2.widget.ViewPager2
import com.ewingsa.ohyeah.appinjection.Injectable
import com.ewingsa.ohyeah.viper.BaseViperFragment
import kotlinx.android.synthetic.main.dialog_licenses.info_dialog_licenses
import kotlinx.android.synthetic.main.fragment_info.info_tutorial_dot_indicator
import kotlinx.android.synthetic.main.fragment_info.info_tutorial_view_pager

class InfoFragment : BaseViperFragment<InfoContract.Presenter, InfoContract.Router>(),
    InfoContract.View, Injectable {

    private var viewDataBinding: ViewDataBinding? = null

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    var dots = mutableListOf<ImageView>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_info, container, false).also {
            viewDataBinding = DataBindingUtil.bind(it)
        }
    }

    override fun setViewModel(infoViewModel: InfoViewModel) {
        viewDataBinding?.setVariable(BR.viewModel, infoViewModel)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDotIndicators(view)
        info_tutorial_view_pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                setDotIndicator(position)
            }
        })
    }

    private fun setupDotIndicators(view: View) {
        val infoTutorialAdapter = InfoAdapter()
        info_tutorial_view_pager.adapter = infoTutorialAdapter
        for (slide in 0 until infoTutorialAdapter.itemCount) {
            dots.add(ImageView(view.context))
            info_tutorial_dot_indicator.addView(dots[slide], DOT_INDICATOR_LAYOUT_PARAMS)
        }
        setDotIndicator(0)
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun setDotIndicator(position: Int) {
        dots[position].setImageResource(R.drawable.active_dot)
        for (slide in 0 until position) {
            dots[slide].setImageResource(R.drawable.inactive_dot)
        }
        for (slide in position + 1 until dots.size) {
            dots[slide].setImageResource(R.drawable.inactive_dot)
        }
    }

    override fun showOpenSourceLicenses() {
        context?.let {
            val view = LayoutInflater.from(it).inflate(R.layout.dialog_licenses, info_dialog_licenses) as? WebView
            view?.loadUrl(OPEN_SOURCE_LICENSES_LOCATION)
            AlertDialog.Builder(it, R.style.Theme_AppCompat_DayNight_Dialog_Alert)
                .setTitle(it.getString(R.string.info_open_source_licenses))
                .setView(view)
                .setPositiveButton(android.R.string.ok, null)
                .show()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(): InfoFragment {
            return InfoFragment()
        }

        private val DOT_INDICATOR_LAYOUT_PARAMS: LayoutParams by lazy {
            LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply {
                setMargins(0, 0, 8, 0)
            }
        }

        private const val OPEN_SOURCE_LICENSES_LOCATION = "file:///android_asset/open_source_licenses.html"
    }
}

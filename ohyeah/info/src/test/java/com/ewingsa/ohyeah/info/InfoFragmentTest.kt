package com.ewingsa.ohyeah.info

import android.widget.ImageView
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class InfoFragmentTest {

    private val firstDot: ImageView = mock()
    private val secondDot: ImageView = mock()
    private val thirdDot: ImageView = mock()
    private val lastDot: ImageView = mock()

    private lateinit var infoFragment: InfoFragment

    @Before
    fun setup() {
        infoFragment = InfoFragment().apply {
            this.dots = mutableListOf(firstDot, secondDot, thirdDot, lastDot)
        }
    }

    @Test
    fun testGetSetDotIndicator_firstPosition() {
        infoFragment.setDotIndicator(0)

        verify(firstDot).setImageResource(R.drawable.active_dot)
        verify(secondDot).setImageResource(R.drawable.inactive_dot)
        verify(thirdDot).setImageResource(R.drawable.inactive_dot)
        verify(lastDot).setImageResource(R.drawable.inactive_dot)
    }

    @Test
    fun testGetSetDotIndicator_thirdPosition() {
        infoFragment.setDotIndicator(2)

        verify(firstDot).setImageResource(R.drawable.inactive_dot)
        verify(secondDot).setImageResource(R.drawable.inactive_dot)
        verify(thirdDot).setImageResource(R.drawable.active_dot)
        verify(lastDot).setImageResource(R.drawable.inactive_dot)
    }

    @Test
    fun testGetSetDotIndicator_lastPosition() {
        infoFragment.setDotIndicator(3)

        verify(firstDot).setImageResource(R.drawable.inactive_dot)
        verify(secondDot).setImageResource(R.drawable.inactive_dot)
        verify(thirdDot).setImageResource(R.drawable.inactive_dot)
        verify(lastDot).setImageResource(R.drawable.active_dot)
    }
}

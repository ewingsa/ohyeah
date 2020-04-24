package com.ewingsa.ohyeah.info

import org.junit.Assert.assertEquals
import org.junit.Test

class InfoAdapterTest {

    @Test
    fun testGetItemCount() {
        val infoAdapter = InfoAdapter()

        assertEquals(NUMBER_OF_TUTORIAL_IMAGES, infoAdapter.itemCount)
    }

    private companion object {
        const val NUMBER_OF_TUTORIAL_IMAGES = 7
    }
}

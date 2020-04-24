package com.ewingsa.ohyeah.setreminder.helpers

import com.ewingsa.ohyeah.setreminder.WheelType
import org.junit.Assert.assertEquals
import org.junit.Test

class WheelComponentHelperTest {

    @Test
    fun testGetTimePosition_hour() {
        assertEquals(INT_MIN - 3, WheelComponentHelper.getTimePosition(WheelType.HOURS, POSITION))
    }

    @Test
    fun testGetTimePosition_minute() {
        assertEquals(INT_MIN - 2, WheelComponentHelper.getTimePosition(WheelType.MINUTES, POSITION))
    }

    @Test
    fun testGetTimePosition_amPm() {
        assertEquals(INT_MIN, WheelComponentHelper.getTimePosition(WheelType.MERIDIES, POSITION))
    }

    private companion object {
        const val INT_MIN = 1073741823
        const val POSITION = 0
    }
}

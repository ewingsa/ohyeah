package com.ewingsa.ohyeah.helpers

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.TimeZone

class DateHelperTest {

    @Before
    fun setup() {
        TimeZone.setDefault(TimeZone.getTimeZone(GMT))
    }

    @Test
    fun testFormatDate_yearMonthDay() {
        val date = DateHelper.formatDate(YEAR, MONTH, DAY)

        assertEquals(DATE, date)
    }

    @Test
    fun testFormatDate_timestamp() {
        val date = DateHelper.formatDate(TIMESTAMP_MILLISECONDS)

        assertEquals(DATE, date)
    }

    @Test
    fun testFormatTime() {
        val time = DateHelper.formatTime(TIMESTAMP_MILLISECONDS)

        assertEquals(TIME, time)
    }

    @Test
    fun testFormatPreviewTime_differentDay() {
        val time = DateHelper.formatPreviewTime(TIMESTAMP_MILLISECONDS)

        assertEquals(MONTH_DAY, time)
    }

    @Test
    fun testFormatDateAndTime() {
        val dateAndTime = DateHelper.formatDateAndTime(TIMESTAMP_MILLISECONDS)

        assertEquals(MONTH_DAY_YEAR_HOUR_MINUTE, dateAndTime)
    }

    private companion object {
        const val DATE = "Fri, Apr 17, 2020"
        const val DAY = 17
        const val GMT = "GMT"
        const val MONTH = 3
        const val MONTH_DAY = "Apr 17"
        const val MONTH_DAY_YEAR_HOUR_MINUTE = "04,17,2020,13,05"
        const val TIME = "1:05 PM"
        const val TIMESTAMP_MILLISECONDS = 1587128700000L
        const val YEAR = 2020
    }
}

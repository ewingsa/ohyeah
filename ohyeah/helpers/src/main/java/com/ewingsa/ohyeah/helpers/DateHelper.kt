package com.ewingsa.ohyeah.helpers

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DateHelper {

    private const val COLON = ":"
    private const val HOUR_MINUTE_MERIDIEM_PATTERN = "h:mm a"
    private const val MONTH_DAY_PATTERN = "MMM dd"
    private const val MONTH_DAY_YEAR_HOUR_MINUTE_PATTERN = "MM,dd,yyyy,H,mm"
    private const val MONTH_DAY_YEAR_PATTERN = "MM:dd:yyyy"
    private const val WEEKDAY_MONTH_DATE_YEAR_PATTERN = "EEE, MMM dd, yyyy"

    /**
     * @return The current unix timestamp
     */
    fun now() = Date().time

    /**
     * @return The current date as integer triple (month, day, year)
     */
    fun currentDate() = formatDate(Date())

    /**
     * @return The date formatted like "Mon, Jan 1, 2020"
     */
    fun formatDate(year: Int, month: Int, dayOfMonth: Int): String {
        val date = Calendar.getInstance().apply {
            set(year, month, dayOfMonth)
        }.time
        return formatDateWithWeekday(date)
    }

    /**
     * @return The date formatted like "Mon, Jan 1, 2020"
     */
    fun formatDate(timestamp: Long): String {
        val date = Date(timestamp)
        return formatDateWithWeekday(date)
    }

    /**
     * @return The time formatted like "12:30 P.M."
     */
    fun formatTime(timestamp: Long): String {
        val date = Date(timestamp)
        val timeFormat = SimpleDateFormat(HOUR_MINUTE_MERIDIEM_PATTERN, Locale.getDefault())
        return timeFormat.format(date)
    }

    /**
     * @return The date formatted like "Jan 1" if it is different than the current day,
     * or a time formatted like "12:30 P.M." if the same day.
     */
    fun formatPreviewTime(timestamp: Long): String {
        return if (isCurrentDate(timestamp)) {
            formatTime(timestamp)
        } else {
            val date = Date(timestamp)
            val timeFormat = SimpleDateFormat(MONTH_DAY_PATTERN, Locale.getDefault())
            timeFormat.format(date)
        }
    }

    /**
     * @return The date and time with comma-separated values. For example, "01,01,2020,13,00".
     */
    fun formatDateAndTime(timestamp: Long): String {
        val date = Date(timestamp)
        val timeFormat = SimpleDateFormat(MONTH_DAY_YEAR_HOUR_MINUTE_PATTERN, Locale.getDefault())
        return timeFormat.format(date)
    }

    private fun formatDate(date: Date): Triple<Int, Int, Int> {
        val timeFormat = SimpleDateFormat(MONTH_DAY_YEAR_PATTERN, Locale.getDefault())
        val currentDate = timeFormat.format(date).split(COLON).map { it.toInt() }
        return Triple(currentDate[0] - 1, currentDate[1], currentDate[2])
    }

    private fun formatDateWithWeekday(date: Date): String {
        val timeFormat = SimpleDateFormat(WEEKDAY_MONTH_DATE_YEAR_PATTERN, Locale.getDefault())
        return timeFormat.format(date)
    }

    private fun isCurrentDate(timestamp: Long) = formatDate(Date(timestamp)) == currentDate()
}

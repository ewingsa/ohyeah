package com.ewingsa.ohyeah.setreminder.helpers

import com.ewingsa.ohyeah.setreminder.WheelType
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object WheelComponentHelper {

    private const val CURRENT_HOUR_OFFSET = -5
    private const val CURRENT_MINUTE_OFFSET = -4
    private const val NO_OFFSET = 0
    private const val PM_OFFSET = 1
    private const val SET_HOUR_OFFSET = -3
    private const val SET_MINUTE_OFFSET = -2

    private const val COLON = ":"
    private const val HOUR_MINUTE_PATTERN = "HH:mm"

    fun getCurrentTimePosition(type: WheelType): Int {

        val timeFormat = SimpleDateFormat(HOUR_MINUTE_PATTERN, Locale.getDefault())
        val currentTime: String = timeFormat.format(Date())
        return (Int.MAX_VALUE / 2) + when (type) {
            WheelType.HOURS -> {
                currentTime.substringBefore(COLON).toInt() + CURRENT_HOUR_OFFSET
            }
            WheelType.MINUTES -> {
                currentTime.substringAfter(COLON).toInt() + CURRENT_MINUTE_OFFSET
            }
            WheelType.MERIDIES -> {
                if (currentTime.substringBefore(COLON).toInt() >= 12) {
                    PM_OFFSET
                } else {
                    NO_OFFSET
                }
            }
        }
    }

    fun getTimePosition(type: WheelType, position: Int): Int {
        return (Int.MAX_VALUE / 2) + position + when (type) {
            WheelType.HOURS -> {
                SET_HOUR_OFFSET
            }
            WheelType.MINUTES -> {
                SET_MINUTE_OFFSET
            }
            WheelType.MERIDIES -> {
                NO_OFFSET
            }
        }
    }
}

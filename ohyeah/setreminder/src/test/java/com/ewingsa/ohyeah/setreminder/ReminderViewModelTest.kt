package com.ewingsa.ohyeah.setreminder

import android.net.Uri
import com.ewingsa.ohyeah.setreminder.SetReminderInteractor.Companion.AM_VALUE
import com.ewingsa.ohyeah.setreminder.SetReminderInteractor.Companion.PM_VALUE
import com.ewingsa.ohyeah.setreminder.helpers.WheelComponentHelper
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class ReminderViewModelTest {

    private val interactions: ReminderViewModel.Interactions = mock()

    private lateinit var reminderViewModel: ReminderViewModel

    @Before
    fun setup() {
        val reminderDataModel = ReminderDataModel(MESSAGE, SENDER, MESSAGE_ID, SENDER_ID, YEAR, MONTH, DAY, HOUR, MINUTE, AM_PM)
        reminderViewModel = ReminderViewModel(reminderDataModel, interactions)
    }

    @Test
    fun testOnBackPress() {
        reminderViewModel.onBackPress.onClick(mock())

        verify(interactions).onBackPress()
    }

    @Test
    fun testOnDeletePress() {
        reminderViewModel.onDeletePress.onClick(mock())

        verify(interactions).onDeletePress()
    }

    @Test
    fun testGetMessage() {
        assertEquals(MESSAGE, reminderViewModel.message)
    }

    @Test
    fun testSetMessage() {
        reminderViewModel.message = NEW_MESSAGE

        assertEquals(NEW_MESSAGE, reminderViewModel.reminderDataModel.message)
    }

    @Test
    fun testGetSender() {
        assertEquals(SENDER, reminderViewModel.sender)
    }

    @Test
    fun testSetSender() {
        reminderViewModel.sender = NEW_SENDER

        assertEquals(NEW_SENDER, reminderViewModel.reminderDataModel.sender)
    }

    @Test
    fun testGetSenderPicture() {
        assertNull(reminderViewModel.senderPicture)
    }

    @Test
    fun testSetSenderPicture() {
        val uri: Uri = mock()
        reminderViewModel.senderPicture = uri

        assertEquals(uri, reminderViewModel.reminderDataModel.senderPicture)
    }

    @Test
    fun testOnDatePress() {
        reminderViewModel.onDatePress.onClick(mock())

        verify(interactions).onDatePress(reminderViewModel)
    }

    @Test
    fun testGetHour() {
        assertEquals(HOUR, reminderViewModel.hour)
    }

    @Test
    fun testGetMinute() {
        assertEquals(MINUTE, reminderViewModel.minute)
    }

    @Test
    fun testGetAmPm() {
        assertEquals(AM_PM, reminderViewModel.amPm)
    }

    @Test
    fun testHourListener() {
        reminderViewModel.hourListener.onItemSelected(NEW_HOUR)

        assertEquals(NEW_HOUR.toInt(), reminderViewModel.reminderDataModel.hour)
    }

    @Test
    fun testMinuteListener() {
        reminderViewModel.minuteListener.onItemSelected(NEW_MINUTE)

        assertEquals(NEW_MINUTE.toInt(), reminderViewModel.reminderDataModel.minute)
    }

    @Test
    fun testAmPmListener_isAm() {
        reminderViewModel.amPmListener.onItemSelected(AM)

        assertEquals(AM_VALUE, reminderViewModel.reminderDataModel.amPm)
    }

    @Test
    fun testAmPmListener_isPm() {
        reminderViewModel.amPmListener.onItemSelected(PM)

        assertEquals(PM_VALUE, reminderViewModel.reminderDataModel.amPm)
    }

    @Test
    fun testGetPosition_noPositionSet() {
        val wheelComponentHelper: WheelComponentHelper = mock()

        val reminderDataModel = ReminderDataModel(MESSAGE, SENDER)
        reminderViewModel = ReminderViewModel(reminderDataModel, interactions)
        reminderViewModel.wheelComponentHelper = wheelComponentHelper

        reminderViewModel.getPosition(WheelType.HOURS)

        verify(wheelComponentHelper).getCurrentTimePosition(WheelType.HOURS)
    }

    @Test
    fun testGetPosition_hour() {
        val wheelComponentHelper: WheelComponentHelper = mock()

        reminderViewModel.wheelComponentHelper = wheelComponentHelper

        reminderViewModel.getPosition(WheelType.HOURS)

        verify(wheelComponentHelper).getTimePosition(WheelType.HOURS, reminderViewModel.hour)
    }

    @Test
    fun testGetPosition_minute() {
        val wheelComponentHelper: WheelComponentHelper = mock()

        reminderViewModel.wheelComponentHelper = wheelComponentHelper

        reminderViewModel.getPosition(WheelType.MINUTES)

        verify(wheelComponentHelper).getTimePosition(WheelType.MINUTES, reminderViewModel.minute)
    }

    @Test
    fun testGetPosition_amPm() {
        val wheelComponentHelper: WheelComponentHelper = mock()

        reminderViewModel.wheelComponentHelper = wheelComponentHelper

        reminderViewModel.getPosition(WheelType.MERIDIES)

        verify(wheelComponentHelper).getTimePosition(WheelType.MERIDIES, reminderViewModel.amPm)
    }

    @Test
    fun testOnSelectPhotoPress() {
        reminderViewModel.onSelectPhotoPress.onClick(mock())

        verify(interactions).onSelectPhotoPress(reminderViewModel)
    }

    @Test
    fun testOnSavePress() {
        reminderViewModel.onSavePress.onClick(mock())

        verify(interactions).onSavePress(reminderViewModel.message)
    }

    private companion object {
        const val AM = "A.M."
        const val AM_PM = AM_VALUE
        const val DAY = 17
        const val HOUR = 13
        const val MESSAGE = "message"
        const val MESSAGE_ID = 1L
        const val MINUTE = 5
        const val MONTH = 3
        const val NEW_HOUR = "2"
        const val NEW_MESSAGE = "new message"
        const val NEW_MINUTE = "6"
        const val NEW_SENDER = "new sender"
        const val PM = "P.M."
        const val SENDER = "sender"
        const val SENDER_ID = 2L
        const val YEAR = 2020
    }
}

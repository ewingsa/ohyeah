package com.ewingsa.ohyeah.setreminder

import android.app.AlarmManager
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources
import com.ewingsa.ohyeah.database.Message
import com.ewingsa.ohyeah.database.Sender
import com.ewingsa.ohyeah.helpers.DateHelper
import com.ewingsa.ohyeah.helpers.IntentHelper
import com.ewingsa.ohyeah.setreminder.SetReminderInteractor.Companion.AM_VALUE
import com.ewingsa.ohyeah.setreminder.SetReminderInteractor.Companion.PM_VALUE
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.eq
import org.mockito.kotlin.isNull
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import com.ewingsa.ohyeah.resources.R as MainR

class SetReminderInteractorTest {

    private var presenter: SetReminderContract.Presenter = mock()
    private var repository: SetReminderContract.Repository = mock()
    private var router: SetReminderContract.Router = mock()
    private var resources: Resources = mock()

    private lateinit var setReminderInteractor: SetReminderInteractor

    @Before
    fun setup() {
        setReminderInteractor = SetReminderInteractor(repository, resources).apply {
            setPresenter(presenter)
            onRouterAttached(router)
        }
    }

    @Test
    fun testLoadReminder_hasData() {
        setReminderInteractor.onNewReminder()

        setReminderInteractor.loadReminder(MESSAGE_ID)

        verify(presenter).addExistingReminderData(any())
    }

    @Test
    fun testLoadReminder_doesNotHaveData() {
        setReminderInteractor.loadReminder(MESSAGE_ID)

        verify(repository).getExistingReminder(MESSAGE_ID)
    }

    @Test
    fun testOnSenderLoaded() {
        val sender = Sender(SENDER_ID, SENDER_NAME, null)

        setReminderInteractor.onSenderLoaded(sender)

        val captor = argumentCaptor<ReminderDataModel>()

        verify(presenter).addNewReminderData(captor.capture())

        assertEquals(SENDER_ID, captor.firstValue.senderId)
        assertEquals(SENDER_NAME, captor.firstValue.sender)
    }

    @Test
    fun testOnReminderLoaded_midnight() {
        testOnReminderLoaded("00", 12, AM_VALUE)
    }

    @Test
    fun testOnReminderLoaded_morning() {
        testOnReminderLoaded("01", 1, AM_VALUE)
    }

    @Test
    fun testOnReminderLoaded_noon() {
        testOnReminderLoaded("12", 12, PM_VALUE)
    }

    @Test
    fun testOnReminderLoaded_afternoon() {
        testOnReminderLoaded("13", 1, PM_VALUE)
    }

    private fun testOnReminderLoaded(hourInput: String, expectedHourOutput: Int, expectedAmPmOutput: Int) {
        val dateHelper: DateHelper = mock()

        whenever(dateHelper.formatDateAndTime(any())).thenReturn("$MONTH,$DAY,$YEAR,$hourInput,$MINUTE")

        setReminderInteractor.dateHelper = dateHelper

        setReminderInteractor.onReminderLoaded(MESSAGE, SENDER)

        val captor = argumentCaptor<ReminderDataModel>()

        verify(presenter).addExistingReminderData(captor.capture())

        assertEquals(expectedHourOutput, captor.firstValue.hour)
        assertEquals(expectedAmPmOutput, captor.firstValue.amPm)

        assertEquals(MESSAGE_TEXT, captor.firstValue.message)
        assertEquals(SENDER_NAME, captor.firstValue.sender)
        assertEquals(MESSAGE_ID, captor.firstValue.messageId)
        assertEquals(SENDER_ID, captor.firstValue.senderId)
        assertEquals(YEAR.toInt(), captor.firstValue.year)
        assertEquals(MONTH.toInt() - 1, captor.firstValue.month)
        assertEquals(DAY.toInt(), captor.firstValue.dayOfMonth)
        assertEquals(MINUTE.toInt(), captor.firstValue.minute)
    }

    @Test
    fun testOnNewReminder() {
        setReminderInteractor.onNewReminder()

        verify(presenter).addNewReminderData(any())
    }

    @Test
    fun testOnNewReminder_existingSender() {
        setReminderInteractor.onNewReminder(SENDER_ID)

        verify(repository).getExistingSender(SENDER_ID)
    }

    @Test
    fun testGoBack() {
        setReminderInteractor.goBack()

        verify(router).goBack()
    }

    @Test
    fun testOnSaveRequest_existingSender() {
        setReminderInteractor.onReminderLoaded(MESSAGE, SENDER)

        setReminderInteractor.onSaveRequest()

        verify(repository).updateSenderName(SENDER_ID, SENDER_NAME)
    }

    @Test
    fun testOnSaveRequest_new_sender_senderNotSpecified() {
        whenever(resources.getString(MainR.string.app_name)).thenReturn(APP_NAME)

        setReminderInteractor.onNewReminder()

        setReminderInteractor.onSaveRequest()

        verify(repository).getExistingConversation(APP_NAME)
    }

    @Test
    fun testOnNewSender_senderSpecified() {
        setReminderInteractor.onReminderLoaded(MESSAGE, SENDER)

        setReminderInteractor.onNewSender()

        val captor = argumentCaptor<Sender>()

        verify(repository).saveNewSender(captor.capture())

        assertEquals(SENDER_NAME, captor.firstValue.name)
    }

    @Test
    fun testOnNewSender_senderNotSpecified() {
        whenever(resources.getString(MainR.string.app_name)).thenReturn(APP_NAME)

        val captor = argumentCaptor<Sender>()

        setReminderInteractor.onNewSender()

        verify(repository).saveNewSender(captor.capture())

        assertEquals(APP_NAME, captor.firstValue.name)
    }

    @Test
    fun testOnSenderSaved_existingReminder() {
        setReminderInteractor.onReminderLoaded(MESSAGE, SENDER)

        val captor = argumentCaptor<Message>()

        setReminderInteractor.onSenderSaved(SENDER_ID)

        verify(repository).updateMessage(captor.capture())
        verify(repository, never()).updateSenderPicture(any(), any())

        assertEquals(MESSAGE_ID, captor.firstValue.messageId)
        assertEquals(MESSAGE_TEXT, captor.firstValue.message)
        assertEquals(SENDER_ID, captor.firstValue.senderId)
    }

    @Test
    fun testOnSenderSaved_newReminder() {
        setReminderInteractor.onSenderSaved(SENDER_ID)

        verify(repository).saveNewMessage(any())
    }

    @Test
    fun testGetTwentyFourHour_midnight() {
        val reminderDataModel = ReminderDataModel(MESSAGE_TEXT, SENDER_NAME, hour = 12, amPm = AM_VALUE)

        assertEquals(0, setReminderInteractor.getTwentyFourHour(reminderDataModel))
    }

    @Test
    fun testGetTwentyFourHour_morning() {
        val reminderDataModel = ReminderDataModel(MESSAGE_TEXT, SENDER_NAME, hour = 1, amPm = AM_VALUE)

        assertEquals(1, setReminderInteractor.getTwentyFourHour(reminderDataModel))
    }

    @Test
    fun testGetTwentyFourHour_noon() {
        val reminderDataModel = ReminderDataModel(MESSAGE_TEXT, SENDER_NAME, hour = 12, amPm = PM_VALUE)

        assertEquals(12, setReminderInteractor.getTwentyFourHour(reminderDataModel))
    }

    @Test
    fun testGetTwentyFourHour_afternoon() {
        val reminderDataModel = ReminderDataModel(MESSAGE_TEXT, SENDER_NAME, hour = 1, amPm = PM_VALUE)

        assertEquals(13, setReminderInteractor.getTwentyFourHour(reminderDataModel))
    }

    @Test
    fun testOnMessageSaved() {
        val context: Context = mock()
        val packageManager: PackageManager = mock()
        val intentHelper: IntentHelper = mock()

        whenever(presenter.getContext()).thenReturn(context)
        whenever(context.packageManager).thenReturn(packageManager)
        whenever(packageManager.getComponentEnabledSetting(any())).thenReturn(PackageManager.COMPONENT_ENABLED_STATE_ENABLED)
        whenever(context.getSystemService(Context.ALARM_SERVICE)).thenReturn(mock<AlarmManager>())

        setReminderInteractor.intentHelper = intentHelper
        setReminderInteractor.onReminderLoaded(MESSAGE, SENDER)

        setReminderInteractor.onMessageSaved(NEW_MESSAGE_ID)

        verify(intentHelper).buildPendingIntent(any(), any(), eq(NEW_MESSAGE_ID))
        verify(router).goBack()
    }

    @Test
    fun testOnMessageUpdated() {
        val context: Context = mock()
        val packageManager: PackageManager = mock()
        val intentHelper: IntentHelper = mock()

        whenever(presenter.getContext()).thenReturn(context)
        whenever(context.packageManager).thenReturn(packageManager)
        whenever(packageManager.getComponentEnabledSetting(any())).thenReturn(PackageManager.COMPONENT_ENABLED_STATE_ENABLED)
        whenever(context.getSystemService(Context.ALARM_SERVICE)).thenReturn(mock<AlarmManager>())

        setReminderInteractor.intentHelper = intentHelper
        setReminderInteractor.onReminderLoaded(MESSAGE, SENDER)

        setReminderInteractor.onMessageUpdated()

        verify(intentHelper).buildPendingIntent(any(), any(), eq(MESSAGE_ID))
        verify(presenter).onUpdated()
    }

    @Test
    fun testDeleteReminder_wasStored() {
        setReminderInteractor.onReminderLoaded(MESSAGE, SENDER)

        setReminderInteractor.deleteReminder()

        verify(repository).deleteReminder(MESSAGE_ID)
    }

    @Test
    fun testDeleteReminder_wasNotStored() {
        setReminderInteractor.deleteReminder()

        verify(router).goBack()
    }

    @Test
    fun testOnReminderDeleted_wasStored() {
        val intentHelper: IntentHelper = mock()

        setReminderInteractor.intentHelper = intentHelper

        whenever(presenter.getContext()).thenReturn(mock())

        setReminderInteractor.onReminderLoaded(MESSAGE, SENDER)

        setReminderInteractor.onReminderDeleted()

        verify(intentHelper).deletePendingIntent(any(), any(), eq(MESSAGE_ID))
        verify(repository).isSenderOrphaned(SENDER_ID)
    }

    @Test
    fun testOnReminderDeleted_wasNotStored() {
        val intentHelper: IntentHelper = mock()

        setReminderInteractor.intentHelper = intentHelper

        whenever(presenter.getContext()).thenReturn(mock())

        setReminderInteractor.onReminderDeleted()

        verify(intentHelper).deletePendingIntent(any(), any(), isNull())
        verify(router).goBack()
    }

    @Test
    fun testOnSenderOrphaned_isOrphaned() {
        setReminderInteractor.onReminderLoaded(MESSAGE, SENDER)

        setReminderInteractor.onSenderOrphaned(true)

        verify(repository).deleteSender(SENDER_ID)
    }

    @Test
    fun testOnSenderOrphaned_notOrphaned() {
        setReminderInteractor.onReminderLoaded(MESSAGE, SENDER)

        setReminderInteractor.onSenderOrphaned(false)

        verify(router).goBack()
    }

    @Test
    fun testOnSenderDeleted() {
        setReminderInteractor.onSenderDeleted()

        verify(router).goBackToMain()
    }

    @Test
    fun testClear() {
        setReminderInteractor.clear()

        verify(repository).clear()
    }

    private companion object {
        const val APP_NAME = "Oh Yeah!"
        const val DAY = "17"
        const val MESSAGE_TEXT = "message"
        const val MESSAGE_ID = 1L
        const val MINUTE = "05"
        const val MONTH = "03"
        const val NEW_MESSAGE_ID = 4L
        const val SENDER_NAME = "sender"
        const val SENDER_ID = 2L
        const val YEAR = "2020"

        val MESSAGE = Message(MESSAGE_ID, MESSAGE_TEXT, SENDER_ID, 3L)
        val SENDER = Sender(SENDER_ID, SENDER_NAME, null)
    }
}

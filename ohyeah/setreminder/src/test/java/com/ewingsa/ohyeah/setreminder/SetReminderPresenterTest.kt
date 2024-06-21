package com.ewingsa.ohyeah.setreminder

import android.content.Context
import android.content.res.Resources
import com.ewingsa.ohyeah.setreminder.helpers.PermissionHelper
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class SetReminderPresenterTest {

    private var view: SetReminderContract.View = mock()
    private var interactor: SetReminderContract.Interactor = mock()
    private var router: SetReminderContract.Router = mock()
    private var resources: Resources = mock()

    private lateinit var setReminderPresenter: SetReminderPresenter

    @Before
    fun setup() {
        setReminderPresenter = SetReminderPresenter(interactor, resources).apply {
            onViewAttached(view, router)
        }
    }

    @Test
    fun testOnViewAttached_newReminder_existingSender() {
        whenever(view.getSenderId()).thenReturn(SENDER_ID)

        setReminderPresenter.onViewAttached(view, router)

        verify(interactor, times(2)).onRouterAttached(router)
        verify(interactor).onNewReminder(SENDER_ID)
    }

    @Test
    fun testOnViewAttached_newReminder_noExistingSender() {
        whenever(view.getSenderId()).thenReturn(null)
        whenever(view.getMessageId()).thenReturn(null)

        setReminderPresenter.onViewAttached(view, router)

        verify(interactor, times(2)).onRouterAttached(router)
        verify(interactor).onNewReminder()
    }

    @Test
    fun testOnViewAttached_existingReminder() {
        whenever(view.getSenderId()).thenReturn(null)
        whenever(view.getMessageId()).thenReturn(MESSAGE_ID)

        setReminderPresenter.onViewAttached(view, router)

        verify(interactor, times(2)).onRouterAttached(router)
        verify(interactor).loadReminder(MESSAGE_ID)
    }

    @Test
    fun testGetContext() {
        val contextMock: Context = mock()

        whenever(view.getContext()).thenReturn(contextMock)

        val context = setReminderPresenter.getContext()

        assertEquals(contextMock, context)
    }

    @Test
    fun testAddExistingReminderData() {
        val reminderDataModel = ReminderDataModel(MESSAGE, SENDER)

        setReminderPresenter.addExistingReminderData(reminderDataModel)

        val captor = argumentCaptor<ReminderViewModel>()
        verify(view).addExistingReminderViewModel(captor.capture())

        assertEquals(reminderDataModel, captor.firstValue.reminderDataModel)
    }

    @Test
    fun testAddNewReminderData() {
        val reminderDataModel = ReminderDataModel(MESSAGE, SENDER)

        setReminderPresenter.addNewReminderData(reminderDataModel)

        val captor = argumentCaptor<ReminderViewModel>()
        verify(view).addNewReminderViewModel(captor.capture())

        assertEquals(reminderDataModel, captor.firstValue.reminderDataModel)
    }

    @Test
    fun testOnBackPress() {
        setReminderPresenter.onBackPress()

        verify(interactor).goBack()
    }

    @Test
    fun testOnDatePress() {
        val reminderDataModel = ReminderDataModel(MESSAGE, SENDER, year = YEAR, month = MONTH, dayOfMonth = DAY)
        val reminderViewModel = ReminderViewModel(reminderDataModel, setReminderPresenter)

        val context: Context = mock()

        whenever(view.getContext()).thenReturn(context)

        setReminderPresenter.onDatePress(reminderViewModel)

        verify(view).showDatePicker(eq(context), any(), eq(YEAR), eq(MONTH), eq(DAY))
    }

    @Test
    fun testOnSelectPhotoPress_needsPermission() {
        val permissionHelper: PermissionHelper = mock()

        setReminderPresenter.permissionHelper = permissionHelper

        whenever(view.getContext()).thenReturn(mock())
        whenever(permissionHelper.hasExternalStoragePermission(any())).thenReturn(false)

        setReminderPresenter.onSelectPhotoPress(mock())

        verify(view).setPicturePickerCallback(any())
        verify(view).onExternalStoragePermissionRequired()
    }

    @Test
    fun testOnSelectPhotoPress_doesNotNeedPermission() {
        val permissionHelper: PermissionHelper = mock()

        setReminderPresenter.permissionHelper = permissionHelper

        whenever(view.getContext()).thenReturn(mock())
        whenever(permissionHelper.hasExternalStoragePermission(any())).thenReturn(true)

        setReminderPresenter.onSelectPhotoPress(mock())

        verify(view).setPicturePickerCallback(any())
        verify(view).showPicturePicker()
    }

    @Test
    fun testOnSavePress_messageFilled() {
        whenever(view.getContext()).thenReturn(mock())

        setReminderPresenter.onSavePress(MESSAGE)

        verify(interactor).onSaveRequest()
    }

    @Test
    fun testOnSavePress_messageEmpty() {
        whenever(view.getContext()).thenReturn(mock())

        setReminderPresenter.onSavePress("")

        verify(view).showNeedsMessage()
    }

    @Test
    fun testOnUpdated() {
        setReminderPresenter.onUpdated()

        verify(view).showUpdatedMessage()
    }

    @Test
    fun testOnViewDetached() {
        setReminderPresenter.onViewDetached()

        verify(interactor).clear()
    }

    private companion object {
        const val DAY = 17
        const val MESSAGE = "message"
        const val MESSAGE_ID = 1L
        const val MONTH = 3
        const val SENDER = "sender"
        const val SENDER_ID = 2L
        const val YEAR = 2020
    }
}

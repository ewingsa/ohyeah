package com.ewingsa.ohyeah.setreminder

import android.content.Context
import android.content.res.Resources
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

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

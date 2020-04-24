package com.ewingsa.ohyeah.setreminder

import android.view.View
import androidx.annotation.VisibleForTesting
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.ewingsa.ohyeah.helpers.DateHelper
import com.ewingsa.ohyeah.setreminder.helpers.WheelComponentHelper
import com.ewingsa.ohyeah.wheelcomponent.WheelComponent

class ReminderViewModel(
    val reminderDataModel: ReminderDataModel,
    private val interactions: Interactions
) : BaseObservable() {

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    var wheelComponentHelper = WheelComponentHelper

    val onBackPress = View.OnClickListener { interactions.onBackPress() }

    val onDeletePress = View.OnClickListener { interactions.onDeletePress() }

    @Bindable
    var message = reminderDataModel.message
        set(value) {
            field = value
            reminderDataModel.message = value
        }

    @Bindable
    var sender = reminderDataModel.sender
        set(value) {
            field = value
            reminderDataModel.sender = value
        }

    @Bindable
    var date = DateHelper.formatDate(reminderDataModel.year, reminderDataModel.month, reminderDataModel.dayOfMonth)
        set(value) {
            field = value
            notifyPropertyChanged(BR.date)
        }

    @Bindable
    var senderPicture = reminderDataModel.senderPicture
        set(value) {
            field = value
            reminderDataModel.senderPicture = value
        }

    val onDatePress = View.OnClickListener {
        interactions.onDatePress(this)
    }

    val hour = reminderDataModel.hour

    val minute = reminderDataModel.minute

    val amPm = reminderDataModel.amPm

    val hourListener = object : WheelComponent.SelectedItemListener {
        override fun onItemSelected(item: String) {
            reminderDataModel.hour = item.trim().toInt()
        }
    }

    val minuteListener = object : WheelComponent.SelectedItemListener {
        override fun onItemSelected(item: String) {
            reminderDataModel.minute = item.trim().toInt()
        }
    }

    val amPmListener = object : WheelComponent.SelectedItemListener {
        override fun onItemSelected(item: String) {
            reminderDataModel.amPm = if (item.first() == AM_FIRST_LETTER) { SetReminderInteractor.AM_VALUE } else { SetReminderInteractor.PM_VALUE }
        }
    }

    fun getPosition(wheelType: WheelType): Int {
        if (reminderDataModel.messageId == null) {
            return wheelComponentHelper.getCurrentTimePosition(wheelType)
        }
        val position = when (wheelType) {
            WheelType.HOURS -> hour
            WheelType.MINUTES -> minute
            WheelType.MERIDIES -> amPm
        }
        return wheelComponentHelper.getTimePosition(wheelType, position)
    }

    val onSelectPhotoPress = View.OnClickListener { interactions.onSelectPhotoPress(this) }

    val onSavePress = View.OnClickListener { interactions.onSavePress(message, sender) }

    fun onDateChosen(year: Int, month: Int, dayOfMonth: Int) {
        reminderDataModel.apply {
            this.year = year
            this.month = month
            this.dayOfMonth = dayOfMonth
        }
        date = DateHelper.formatDate(year, month, dayOfMonth)
    }

    interface Interactions {
        fun onBackPress()
        fun onDeletePress()
        fun onDatePress(reminderViewModel: ReminderViewModel)
        fun onSelectPhotoPress(reminderViewModel: ReminderViewModel)
        fun onSavePress(message: String, sender: String)
    }

    private companion object {
        const val AM_FIRST_LETTER = 'A'
    }
}

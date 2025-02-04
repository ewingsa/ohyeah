package com.ewingsa.ohyeah.setreminder

import android.app.DatePickerDialog
import android.content.Context
import android.net.Uri
import com.ewingsa.ohyeah.database.Message
import com.ewingsa.ohyeah.database.Sender
import com.ewingsa.ohyeah.viper.ViperContract

interface SetReminderContract {

    interface View : ViperContract.View {
        fun getSenderId(): Long?
        fun getMessageId(): Long?
        fun addExistingReminderViewModel(viewModel: ReminderViewModel)
        fun addNewReminderViewModel(viewModel: ReminderViewModel)
        fun getContext(): Context?
        fun showDatePicker(context: Context, listener: DatePickerDialog.OnDateSetListener, year: Int, month: Int, dayOfMonth: Int)
        fun setPicturePickerCallback(listener: (Uri) -> Unit)
        fun showPicturePicker()
        fun showNeedsMessage()
        fun showUpdatedMessage()
    }

    interface Presenter : ViperContract.Presenter {
        fun getContext(): Context?
        fun addExistingReminderData(reminderDataModel: ReminderDataModel)
        fun addNewReminderData(reminderDataModel: ReminderDataModel)
        fun onUpdated()
    }

    interface Interactor : ViperContract.Interactor {
        fun loadReminder(messageId: Long)
        fun onSenderLoaded(sender: Sender)
        fun onReminderLoaded(message: Message, sender: Sender)
        fun onNewReminder()
        fun onNewReminder(senderId: Long)
        fun goBack()
        fun onSaveRequest()
        fun onNewSender()
        fun onSenderSaved(senderId: Long)
        fun onMessageSaved(messageId: Long)
        fun onMessageUpdated()
        fun deleteReminder()
        fun onReminderDeleted()
        fun onSenderOrphaned(isOrphaned: Boolean)
        fun onSenderDeleted()
        fun clear()
    }

    interface Repository : ViperContract.Repository {
        fun getExistingReminder(messageId: Long)
        fun getExistingSender(senderId: Long)
        fun getExistingConversation(name: String)
        fun saveNewSender(sender: Sender)
        fun saveNewMessage(message: Message)
        fun updateMessage(message: Message)
        fun updateSenderName(senderId: Long, name: String)
        fun updateSenderPicture(senderId: Long, photoUri: String)
        fun deleteReminder(messageId: Long)
        fun isSenderOrphaned(senderId: Long)
        fun deleteSender(senderId: Long)
        fun clear()
    }

    interface Router : ViperContract.Router {
        fun goBack()
        fun goBackToMain()
    }
}

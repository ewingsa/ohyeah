package com.ewingsa.ohyeah.setreminder

import android.app.AlarmManager
import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_ENABLED
import android.content.pm.PackageManager.DONT_KILL_APP
import android.content.res.Resources
import androidx.annotation.VisibleForTesting
import com.ewingsa.ohyeah.database.Message
import com.ewingsa.ohyeah.database.Sender
import com.ewingsa.ohyeah.helpers.DateHelper
import com.ewingsa.ohyeah.helpers.IntentHelper
import com.ewingsa.ohyeah.receiver.AlarmReceiver
import com.ewingsa.ohyeah.viper.ViperContract
import java.util.Calendar
import javax.inject.Inject
import com.ewingsa.ohyeah.resources.R as MainR

class SetReminderInteractor @Inject constructor(
    private val repository: SetReminderContract.Repository,
    private val resources: Resources
) : SetReminderContract.Interactor {

    private var presenter: SetReminderContract.Presenter? = null

    private var router: SetReminderContract.Router? = null

    private var reminderDataModel: ReminderDataModel? = null

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    var dateHelper = DateHelper

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    var intentHelper = IntentHelper

    init {
        repository.setInteractor(this)
    }

    override fun setPresenter(presenter: ViperContract.Presenter) {
        this.presenter = presenter as? SetReminderContract.Presenter
    }

    override fun onRouterAttached(router: ViperContract.Router?) {
        this.router = router as? SetReminderContract.Router?
    }

    override fun loadReminder(messageId: Long) {
        reminderDataModel?.let {
            presenter?.addExistingReminderData(it)
        } ?: repository.getExistingReminder(messageId)
    }

    override fun onSenderLoaded(sender: Sender) {
        val reminderDataModel = ReminderDataModel(
            "",
            sender.name,
            senderId = sender.senderId,
            senderPicture = sender.photoUri,
            year = dateHelper.currentDate().third,
            month = dateHelper.currentDate().first,
            dayOfMonth = dateHelper.currentDate().second
        )
        this.reminderDataModel = reminderDataModel
        presenter?.addNewReminderData(reminderDataModel)
    }

    override fun onReminderLoaded(message: Message, sender: Sender) {
        val time = dateHelper.formatDateAndTime(message.timestamp).split(COMMA)
        val hourInt = time[3].toInt()
        val hour = if (hourInt % 12 == 0) { HOUR_TWELVE } else { hourInt % HOUR_TWELVE }
        val amPm = if (hourInt < HOUR_TWELVE) { AM_VALUE } else { PM_VALUE }
        val reminderDataModel = ReminderDataModel(
            message.message,
            sender.name,
            message.messageId,
            sender.senderId,
            time[2].toInt(),
            time[0].toInt() - 1,
            time[1].toInt(),
            hour,
            time[4].toInt(),
            amPm,
            sender.photoUri
        )
        this.reminderDataModel = reminderDataModel
        presenter?.addExistingReminderData(reminderDataModel)
    }

    override fun onNewReminder() {
        val reminderDataModel = this.reminderDataModel ?: ReminderDataModel(
            "",
            "",
            year = dateHelper.currentDate().third,
            month = dateHelper.currentDate().first,
            dayOfMonth = dateHelper.currentDate().second
        )
        this.reminderDataModel = reminderDataModel // Restore unsaved information
        presenter?.addNewReminderData(reminderDataModel)
    }

    override fun onNewReminder(senderId: Long) {
        repository.getExistingSender(senderId)
    }

    override fun goBack() {
        router?.goBack()
    }

    override fun onSaveRequest() {
        reminderDataModel?.let {
            if (it.sender.isEmpty()) {
                it.sender = resources.getString(MainR.string.app_name)
            }
            it.senderId?.let { senderId ->
                onSenderSaved(senderId)
            } ?: repository.getExistingConversation(it.sender)
        }
    }

    override fun onNewSender() {
        repository.saveNewSender(
            Sender(
                0,
                reminderDataModel?.sender ?: resources.getString(MainR.string.app_name),
                reminderDataModel?.senderPicture?.toString()
            )
        )
    }

    override fun onSenderSaved(senderId: Long) {
        reminderDataModel?.senderId = senderId
        saveNewOrUpdateOld()
    }

    private fun saveNewOrUpdateOld() {
        val message = reminderDataModel?.message ?: ""
        val senderId = reminderDataModel?.senderId ?: 0L
        val timestamp = getSelectedTimeInMillis()
        reminderDataModel?.messageId?.let {
            val updatedMessage = Message(
                it,
                message,
                senderId,
                timestamp
            )
            repository.updateMessage(updatedMessage)
            updateSender()
        } ?: run {
            val newMessage = Message(
                0L,
                message,
                senderId,
                timestamp
            )
            repository.saveNewMessage(newMessage)
        }
    }

    private fun getSelectedTimeInMillis(): Long {
        return reminderDataModel?.let {
            Calendar.getInstance().apply {
                set(it.year, it.month, it.dayOfMonth, getTwentyFourHour(it), it.minute)
            }.timeInMillis
        } ?: 0L
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun getTwentyFourHour(reminderDataModel: ReminderDataModel): Int {
        return if (reminderDataModel.amPm == PM_VALUE) {
            if (reminderDataModel.hour == HOUR_TWELVE) {
                HOUR_TWELVE
            } else {
                reminderDataModel.hour + HOUR_TWELVE
            }
        } else {
            reminderDataModel.hour % HOUR_TWELVE
        }
    }

    private fun updateSender() {
        reminderDataModel?.let {
            it.senderId?.let { senderId ->
                repository.updateSenderName(senderId, it.sender)
                it.senderPicture?.let { uri ->
                    repository.updateSenderPicture(senderId, uri.toString())
                }
            }
        }
    }

    override fun onMessageSaved(messageId: Long) {
        reminderDataModel?.messageId = messageId
        setAlarm()
        router?.goBack()
    }

    override fun onMessageUpdated() {
        setAlarm()
        presenter?.onUpdated()
    }

    private fun setAlarm() {
        presenter?.getContext()?.let {
            val componentName = ComponentName(it, AlarmReceiver::class.java)
            if (it.packageManager.getComponentEnabledSetting(componentName) != COMPONENT_ENABLED_STATE_ENABLED) {
                it.packageManager.setComponentEnabledSetting(
                    componentName,
                    COMPONENT_ENABLED_STATE_ENABLED,
                    DONT_KILL_APP
                )
            }

            (it.getSystemService(Context.ALARM_SERVICE) as? AlarmManager)?.let { alarmManager ->
                intentHelper.buildPendingIntent(it, AlarmReceiver::class.java, reminderDataModel?.messageId)?.let { pendingIntent ->
                    alarmManager.set(AlarmManager.RTC_WAKEUP, getSelectedTimeInMillis(), pendingIntent)
                }
            }
        }
    }

    override fun deleteReminder() {
        reminderDataModel?.messageId?.let {
            repository.deleteReminder(it)
        } ?: router?.goBack()
    }

    override fun onReminderDeleted() {
        deleteAlarm()
        reminderDataModel?.senderId?.let {
            repository.isSenderOrphaned(it)
        } ?: router?.goBack()
    }

    private fun deleteAlarm() {
        presenter?.getContext()?.let { intentHelper.deletePendingIntent(it, AlarmReceiver::class.java, reminderDataModel?.messageId) }
    }

    override fun onSenderOrphaned(isOrphaned: Boolean) {
        if (isOrphaned) {
            reminderDataModel?.senderId?.let {
                repository.deleteSender(it)
            }
        } else {
            router?.goBack()
        }
    }

    override fun onSenderDeleted() {
        router?.goBackToMain()
    }

    override fun onRouterDetached() {
        router = null
    }

    override fun clear() {
        repository.clear()
    }

    companion object {
        const val AM_VALUE = 0
        const val PM_VALUE = 1

        private const val COMMA = ","
        private const val HOUR_TWELVE = 12
    }
}

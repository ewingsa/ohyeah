package com.ewingsa.ohyeah.setreminder

import android.app.DatePickerDialog
import android.content.Context
import android.content.res.Resources
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AlertDialog
import com.ewingsa.ohyeah.setreminder.helpers.PermissionHelper
import com.ewingsa.ohyeah.viper.ViperContract
import javax.inject.Inject

class SetReminderPresenter @Inject constructor(
    private val interactor: SetReminderContract.Interactor,
    private val resources: Resources
) : SetReminderContract.Presenter, ReminderViewModel.Interactions {

    private var view: SetReminderContract.View? = null

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    var permissionHelper = PermissionHelper

    init {
        interactor.setPresenter(this)
    }

    override fun onViewAttached(view: ViperContract.View, router: ViperContract.Router?) {
        this.view = view as? SetReminderContract.View?

        interactor.onRouterAttached(router)

        this.view?.getSenderId()?.let { interactor.onNewReminder(it) }
        this.view?.getMessageId()?.let { interactor.loadReminder(it) } ?: interactor.onNewReminder()
    }

    override fun getContext(): Context? {
        return view?.getContext()
    }

    override fun addExistingReminderData(reminderDataModel: ReminderDataModel) {
        view?.addExistingReminderViewModel(ReminderViewModel(reminderDataModel, this))
    }

    override fun addNewReminderData(reminderDataModel: ReminderDataModel) {
        view?.addNewReminderViewModel(ReminderViewModel(reminderDataModel, this))
    }

    override fun onBackPress() {
        interactor.goBack()
    }

    override fun onDeletePress() {
        getContext()?.let {
            AlertDialog.Builder(it)
                .setTitle(resources.getString(R.string.set_reminder_delete_reminder))
                .setMessage(resources.getString(R.string.set_reminder_delete_reminder_message))
                .setIcon(R.drawable.ic_ohyeah)
                .setPositiveButton(android.R.string.yes) { _, whichButton ->
                    if (whichButton == -1) {
                        interactor.deleteReminder()
                    }
                }
                .setNegativeButton(android.R.string.no, null).show()
        }
    }

    override fun onDatePress(reminderViewModel: ReminderViewModel) {
        val listener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            reminderViewModel.onDateChosen(year, month, dayOfMonth)
        }
        reminderViewModel.reminderDataModel.run {
            view?.getContext()?.let {
                view?.showDatePicker(it, listener, year, month, dayOfMonth)
            }
        }
    }

    override fun onSelectPhotoPress(reminderViewModel: ReminderViewModel) {
        view?.setPicturePickerCallback { senderPicture -> reminderViewModel.senderPicture = senderPicture }
        if (!permissionHelper.hasExternalStoragePermission(view?.getContext())) {
            view?.onExternalStoragePermissionRequired()
        } else {
            view?.showPicturePicker()
        }
    }

    override fun onSavePress(message: String, sender: String) {
        if (message.isNotEmpty()) {
            view?.getContext()?.let {
                interactor.onSaveRequest()
            }
        } else {
            view?.showNeedsMessage()
        }
    }

    override fun onUpdated() {
        view?.showUpdatedMessage()
    }

    override fun onViewDetached() {
        view = null

        interactor.clear()
    }
}

package com.ewingsa.ohyeah.setreminder

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.ewingsa.ohyeah.appinjection.Injectable
import com.ewingsa.ohyeah.helpers.IntentHelper
import com.ewingsa.ohyeah.setreminder.helpers.PermissionHelper
import com.ewingsa.ohyeah.viper.BaseViperFragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_set_reminder.set_reminder_am_pm
import kotlinx.android.synthetic.main.fragment_set_reminder.set_reminder_hour
import kotlinx.android.synthetic.main.fragment_set_reminder.set_reminder_minute

class SetReminderFragment : BaseViperFragment<SetReminderContract.Presenter, SetReminderContract.Router>(),
    SetReminderContract.View, Injectable {

    private var senderId: Long? = null
    private var messageId: Long? = null

    private var viewDataBinding: ViewDataBinding? = null

    private var picturePickerCallback: ((Uri) -> Unit)? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_set_reminder, container, false).also {
            viewDataBinding = DataBindingUtil.bind(it)
        }
    }

    override fun getSenderId() = senderId

    override fun getMessageId() = messageId

    override fun addExistingReminderViewModel(viewModel: ReminderViewModel) {
        viewDataBinding?.setVariable(BR.viewModel, viewModel)
        set_reminder_minute.position = viewModel.minute
        set_reminder_hour.position = viewModel.hour
        set_reminder_am_pm.position = viewModel.amPm
    }

    override fun addNewReminderViewModel(viewModel: ReminderViewModel) {
        viewDataBinding?.setVariable(BR.viewModel, viewModel)
    }

    override fun showDatePicker(context: Context, listener: DatePickerDialog.OnDateSetListener, year: Int, month: Int, dayOfMonth: Int) {
        DatePickerDialog(context, listener, year, month, dayOfMonth).show()
    }

    override fun setPicturePickerCallback(listener: (Uri) -> Unit) {
        picturePickerCallback = listener
    }

    override fun onExternalStoragePermissionRequired() {
        PermissionHelper.requestExternalStoragePermission(this)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (PermissionHelper.checkExternalStoragePermissionGranted(requestCode, grantResults)) {
            showPicturePicker()
        }
    }

    override fun showPicturePicker() {
        val selectPictureIntent = IntentHelper.buildSelectPictureIntent()
        startActivityForResult(Intent.createChooser(selectPictureIntent, getString(R.string.set_reminder_select_photo)), PICK_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE) {
            data?.data?.let {
                picturePickerCallback?.invoke(it)
            }
        }
    }

    override fun showNeedsMessage() {
        view?.let {
            Snackbar.make(it, getString(R.string.set_reminder_needs_message), Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun showUpdatedMessage() {
        view?.let {
            Snackbar.make(it, getString(R.string.set_reminder_updated), Snackbar.LENGTH_SHORT).show()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(): SetReminderFragment {
            return SetReminderFragment()
        }

        @JvmStatic
        fun newInstance(senderId: Long?, messageId: Long?): SetReminderFragment {
            return SetReminderFragment().apply {
                this.senderId = senderId
                this.messageId = messageId
            }
        }

        private const val PICK_IMAGE = 2
    }
}

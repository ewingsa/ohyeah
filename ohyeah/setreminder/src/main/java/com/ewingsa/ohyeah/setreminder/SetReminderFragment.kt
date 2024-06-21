package com.ewingsa.ohyeah.setreminder

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.VisibleForTesting
import com.ewingsa.ohyeah.appinjection.Injectable
import com.ewingsa.ohyeah.helpers.IntentHelper
import com.ewingsa.ohyeah.setreminder.databinding.FragmentSetReminderBinding
import com.ewingsa.ohyeah.setreminder.helpers.PermissionHelper
import com.ewingsa.ohyeah.viper.BaseViperFragment
import com.google.android.material.snackbar.Snackbar

class SetReminderFragment :
    BaseViperFragment<SetReminderContract.Presenter, SetReminderContract.Router>(),
    SetReminderContract.View,
    Injectable {

    private var senderId: Long? = null
    private var messageId: Long? = null

    private var binding: FragmentSetReminderBinding? = null

    private var picturePickerCallback: ((Uri) -> Unit)? = null

    private var activityPermissionResultLauncher: ActivityResultLauncher<Array<String>>? = null
    private var activityChooserResultLauncher: ActivityResultLauncher<Intent>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityPermissionResultLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { map ->
            if (map.values.all { it }) showPicturePicker()
        }
        activityChooserResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            onPicturePickerResult(result)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = FragmentSetReminderBinding.inflate(inflater, container, false).apply { lifecycleOwner = viewLifecycleOwner }

        return binding?.root
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    override fun getSenderId() = senderId

    override fun getMessageId() = messageId

    override fun addExistingReminderViewModel(viewModel: ReminderViewModel) {
        binding?.setVariable(BR.viewModel, viewModel)
        binding?.setReminderMinute?.position = viewModel.minute
        binding?.setReminderHour?.position = viewModel.hour
        binding?.setReminderAmPm?.position = viewModel.amPm
    }

    override fun addNewReminderViewModel(viewModel: ReminderViewModel) {
        binding?.setVariable(BR.viewModel, viewModel)
    }

    override fun showDatePicker(context: Context, listener: DatePickerDialog.OnDateSetListener, year: Int, month: Int, dayOfMonth: Int) {
        DatePickerDialog(context, listener, year, month, dayOfMonth).show()
    }

    override fun setPicturePickerCallback(listener: (Uri) -> Unit) {
        picturePickerCallback = listener
    }

    override fun onExternalStoragePermissionRequired() {
        activityPermissionResultLauncher?.let {
            PermissionHelper.requestExternalStoragePermission(it) {
                showPicturePicker()
            }
        }
    }

    override fun showPicturePicker() {
        val selectPictureIntent = IntentHelper.buildSelectPictureIntent()
        val picturePickerIntent = Intent.createChooser(selectPictureIntent, getString(R.string.set_reminder_select_photo))
        activityChooserResultLauncher?.launch(picturePickerIntent)
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun onPicturePickerResult(activityResult: ActivityResult) {
        activityResult.data?.data?.let {
            if (SDK_INT >= Build.VERSION_CODES.KITKAT) {
                activity?.contentResolver?.takePersistableUriPermission(it, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            binding?.setReminderPicturePreview?.setImageURI(it) // ensures view is updated
            picturePickerCallback?.invoke(it)
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
    }
}

package com.ewingsa.ohyeah.setreminder

import android.app.DatePickerDialog
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import com.ewingsa.ohyeah.appinjection.Injectable
import com.ewingsa.ohyeah.helpers.DrawableHelper
import com.ewingsa.ohyeah.setreminder.databinding.FragmentSetReminderBinding
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

    private var picturePickerLauncher: ActivityResultLauncher<PickVisualMediaRequest>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        picturePickerLauncher = registerForActivityResult(PickVisualMedia()) { uri ->
            uri?.let {
                picturePickerCallback?.invoke(it)
                binding?.setReminderPicturePreview?.setImageURI(it) // ensures view is updated
            }
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
        setSenderPicture(viewModel.senderPicture.toString())
    }

    override fun addNewReminderViewModel(viewModel: ReminderViewModel) {
        binding?.setVariable(BR.viewModel, viewModel)
        setSenderPicture(viewModel.senderPicture.toString())
    }

    private fun setSenderPicture(uri: String) {
        DrawableHelper.getSmallBitmap(uri, context)?.let {
            binding?.setReminderPicturePreview?.setImageBitmap(it)
        }
    }

    override fun showDatePicker(context: Context, listener: DatePickerDialog.OnDateSetListener, year: Int, month: Int, dayOfMonth: Int) {
        DatePickerDialog(context, listener, year, month, dayOfMonth).show()
    }

    override fun setPicturePickerCallback(listener: (Uri) -> Unit) {
        picturePickerCallback = listener
    }

    override fun showPicturePicker() {
        picturePickerLauncher?.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
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

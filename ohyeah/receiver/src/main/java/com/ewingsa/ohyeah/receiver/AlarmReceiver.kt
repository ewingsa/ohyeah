package com.ewingsa.ohyeah.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.annotation.VisibleForTesting
import com.ewingsa.ohyeah.appinjection.qualifiers.IoThreadScheduler
import com.ewingsa.ohyeah.appinjection.qualifiers.MainThreadScheduler
import com.ewingsa.ohyeah.database.MessageDao
import com.ewingsa.ohyeah.helpers.DrawableHelper
import com.ewingsa.ohyeah.helpers.IntentHelper
import com.ewingsa.ohyeah.push.PushNotificationHelper
import dagger.android.AndroidInjection
import io.reactivex.Scheduler
import javax.inject.Inject

class AlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var messageDao: MessageDao

    @Inject
    @MainThreadScheduler
    lateinit var mainThreadScheduler: Scheduler

    @Inject
    @IoThreadScheduler
    lateinit var ioThreadScheduler: Scheduler

    override fun onReceive(context: Context?, intent: Intent?) {
        AndroidInjection.inject(this, context)

        queryMessageAndReminderNotification(context, intent)
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun queryMessageAndReminderNotification(
        context: Context?,
        intent: Intent?,
        drawableHelper: DrawableHelper = DrawableHelper,
        pushNotificationHelper: PushNotificationHelper = PushNotificationHelper
    ) {
        intent?.getLongExtra(IntentHelper.MESSAGE_ID, 0)?.let { messageId ->

            // This will be disposed when the receiver is terminated
            messageDao.findMessageWithPicture(messageId)
                .subscribeOn(ioThreadScheduler)
                .observeOn(mainThreadScheduler)
                .subscribe(
                    { message ->
                        val text = message.message.message
                        val senderName = message.sender.name
                        val senderId = message.sender.senderId
                        val photoBitmap = drawableHelper.getSmallBitmap(message.sender.photoUri, context)
                        context?.let {
                            pushNotificationHelper.renderPushNotification(it, text, senderName, senderId, messageId, photoBitmap)
                        }
                    },
                    {}
                )
        }
    }
}

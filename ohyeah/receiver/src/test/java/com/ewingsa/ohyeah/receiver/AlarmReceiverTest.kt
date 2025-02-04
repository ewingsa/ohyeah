package com.ewingsa.ohyeah.receiver

import android.content.Context
import android.content.Intent
import com.ewingsa.ohyeah.database.Message
import com.ewingsa.ohyeah.database.MessageDao
import com.ewingsa.ohyeah.database.Sender
import com.ewingsa.ohyeah.database.SenderMessage
import com.ewingsa.ohyeah.helpers.DrawableHelper
import com.ewingsa.ohyeah.helpers.IntentHelper
import com.ewingsa.ohyeah.push.PushNotificationHelper
import io.reactivex.Maybe
import io.reactivex.schedulers.Schedulers.trampoline
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.eq
import org.mockito.kotlin.isNull
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class AlarmReceiverTest {

    private var messageDao: MessageDao = mock()

    private lateinit var alarmReceiver: AlarmReceiver

    @Before
    fun setup() {
        alarmReceiver = AlarmReceiver().apply {
            mainThreadScheduler = trampoline()
            ioThreadScheduler = trampoline()
        }
        alarmReceiver.messageDao = messageDao
    }

    @Test
    fun testQueryMessageAndReminderNotification() {
        val drawableHelper: DrawableHelper = mock()
        val pushNotificationHelper: PushNotificationHelper = mock()
        val context: Context = mock()
        val intent: Intent = mock()

        val senderMessage = SenderMessage(Sender(SENDER_ID, SENDER, null), Message(MESSAGE_ID, MESSAGE, SENDER_ID, TIMESTAMP))
        val senderMessageMaybe = Maybe.just(senderMessage)

        whenever(intent.getLongExtra(IntentHelper.MESSAGE_ID, 0)).thenReturn(MESSAGE_ID)
        whenever(messageDao.findMessageWithPicture(MESSAGE_ID)).thenReturn(senderMessageMaybe)

        alarmReceiver.queryMessageAndReminderNotification(context, intent, drawableHelper, pushNotificationHelper)

        verify(pushNotificationHelper).renderPushNotification(eq(context), eq(MESSAGE), eq(SENDER), eq(SENDER_ID), eq(MESSAGE_ID), isNull())
    }

    private companion object {
        const val MESSAGE = "message"
        const val MESSAGE_ID = 1L
        const val SENDER = "sender"
        const val SENDER_ID = 2L
        const val TIMESTAMP = 3L
    }
}

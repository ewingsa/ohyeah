package com.ewingsa.ohyeah.helpers

import android.app.PendingIntent
import android.content.Context
import android.content.Intent

object IntentHelper {

    const val MESSAGE_ID = "message_id"

    private const val REQUEST_CODE = 0

    fun buildPendingIntent(context: Context, receiverClass: Class<out Any>, messageId: Long?): PendingIntent? {
        val intent = Intent(context, receiverClass)
        intent.putExtra(MESSAGE_ID, messageId)
        return PendingIntent.getBroadcast(
            context,
            REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    fun deletePendingIntent(context: Context, receiverClass: Class<out Any>, messageId: Long?) {
        buildPendingIntent(context, receiverClass, messageId)?.cancel()
    }
}

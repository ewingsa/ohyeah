package com.ewingsa.ohyeah.helpers

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.KITKAT

object IntentHelper {

    const val MESSAGE_ID = "message_id"

    private const val IMAGE_RESOURCE_TYPE = "image/*"
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

    fun buildSelectPictureIntent(): Intent {
        val intent = Intent()
        intent.type = IMAGE_RESOURCE_TYPE
        val action = if (SDK_INT >= KITKAT) { Intent.ACTION_OPEN_DOCUMENT } else { Intent.ACTION_GET_CONTENT }
        intent.action = action
        return intent
    }
}

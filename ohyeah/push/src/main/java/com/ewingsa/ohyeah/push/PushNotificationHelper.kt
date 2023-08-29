package com.ewingsa.ohyeah.push

import android.Manifest
import android.app.Notification.VISIBILITY_PUBLIC
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.O
import android.os.Build.VERSION_CODES.Q
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.ewingsa.ohyeah.resources.R as MainR

object PushNotificationHelper {

    private const val MAIN_ACTIVITY_CLASS_NAME = "com.ewingsa.ohyeah.MainActivity"
    private const val REMINDERS_CHANNEL_ID = "0"

    fun renderPushNotification(context: Context, text: String, senderName: String, senderId: Long, messageId: Long, icon: Bitmap?) {
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        if (SDK_INT >= O) {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager

            val attributes = AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION).build()

            val channel = NotificationChannel(REMINDERS_CHANNEL_ID, context.getString(MainR.string.reminders), NotificationManager.IMPORTANCE_HIGH)
            channel.enableLights(true)
            channel.enableVibration(true)
            channel.lockscreenVisibility = VISIBILITY_PUBLIC
            channel.setShowBadge(true)
            channel.setSound(defaultSoundUri, attributes)

            if (SDK_INT >= Q) {
                channel.setAllowBubbles(true)
            }

            notificationManager?.createNotificationChannel(channel)
        }

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(senderId.toString())).apply {
            setClassName(context, MAIN_ACTIVITY_CLASS_NAME) // If the class itself was specified, the push module would depend on the app module
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val notificationBuilder = NotificationCompat.Builder(context, REMINDERS_CHANNEL_ID)
            .setSmallIcon(MainR.drawable.ic_ohyeah)
            .setContentTitle(senderName)
            .setContentText(text)
            .setSound(defaultSoundUri, AudioManager.STREAM_NOTIFICATION)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        icon?.let {
            notificationBuilder.setLargeIcon(it)
        }

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            NotificationManagerCompat.from(context).notify(messageId.toInt(), notificationBuilder.build())
        }
    }
}

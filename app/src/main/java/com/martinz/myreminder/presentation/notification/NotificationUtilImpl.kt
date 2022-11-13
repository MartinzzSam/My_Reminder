package com.martinz.myreminder.presentation.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ClipDescription
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.martinz.myreminder.R
import com.martinz.myreminder.presentation.MainActivity


class NotificationUtilImpl(
    private val context : Context,
) : NotificationUtil {

    private val notificationManager : NotificationManager = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        context.getSystemService(NotificationManager::class.java) as NotificationManager
    } else {
        TODO("VERSION.SDK_INT < M")
    }


    override fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                APP_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply { setShowBadge(true) }
            notificationChannel.enableVibration(true)

            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    override fun createNotification(notificationId: Int , title: String, description: String) {

        val intent = Intent(context , MainActivity::class.java)

    val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        PendingIntent.getActivity(
            context,
            REQUEST_CODE,
            intent,
            PendingIntent.FLAG_MUTABLE
        )
    } else {

        PendingIntent.getActivity(
            context,
            REQUEST_CODE,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
    }


        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_launcher_background)
        .setContentTitle(title)
        .setContentText(description)
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
    notificationManager.notify(notificationId, builder.build())

    }

    override fun dismissNotification(notificationId: Int) {
        notificationManager.cancel(notificationId)
    }



    companion object {
        const val CHANNEL_ID = "Reminder"
        const val APP_CHANNEL_NAME = "ReminderAppChannel"
        const val REQUEST_CODE = 2731
    }
}
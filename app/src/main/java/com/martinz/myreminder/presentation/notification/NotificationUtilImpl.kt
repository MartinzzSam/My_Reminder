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
import com.google.gson.Gson
import com.martinz.myreminder.R
import com.martinz.myreminder.domain.model.Reminder
import com.martinz.myreminder.presentation.MainActivity
import com.martinz.myreminder.presentation.ReminderDetailActivity


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

    override fun createNotification(reminder : Reminder) {


        val detailIntent = Intent(context, ReminderDetailActivity::class.java)
        val json = Gson().toJson(reminder)
        detailIntent.putExtra("reminder" , json)


    val pendingIntent = PendingIntent.getActivity(
            context,
            REQUEST_CODE,
            detailIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_baseline_location_on_24)
        .setContentTitle(reminder.title)
        .setContentText(reminder.description)
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
    notificationManager.notify(reminder.id, builder.build())

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
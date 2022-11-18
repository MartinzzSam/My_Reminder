package com.martinz.myreminder.presentation.notification

import com.martinz.myreminder.domain.model.Reminder

interface NotificationUtil {


    fun createNotificationChannel()


    fun createNotification(reminder : Reminder)

    fun dismissNotification(notificationId: Int)

}
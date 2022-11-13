package com.martinz.myreminder.presentation.notification

interface NotificationUtil {


    fun createNotificationChannel()


    fun createNotification(notificationId: Int, title: String, description: String)

    fun dismissNotification(notificationId: Int)

}
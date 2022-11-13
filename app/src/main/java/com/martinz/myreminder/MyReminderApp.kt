package com.martinz.myreminder

import android.app.Application
import com.google.firebase.auth.FirebaseAuth
import com.martinz.myreminder.presentation.notification.NotificationUtil
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MyReminderApp : Application() {

    @Inject
    lateinit var notificationUtil: NotificationUtil


    override fun onCreate() {
        super.onCreate()
        notificationUtil.createNotificationChannel()
    }
}
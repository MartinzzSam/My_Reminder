package com.martinz.myreminder.di

import android.app.Application
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.martinz.myreminder.data.local.ReminderDatabase
import com.martinz.myreminder.presentation.geofence.GeofenceUtil
import com.martinz.myreminder.presentation.geofence.GeofenceUtilImpl
import com.martinz.myreminder.presentation.notification.NotificationUtil
import com.martinz.myreminder.presentation.notification.NotificationUtilImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth() = FirebaseAuth.getInstance()

    @Singleton
    @Provides
    fun provideDatabase(app : Application) : ReminderDatabase = Room.databaseBuilder(app ,
        ReminderDatabase::class.java,
        "location_reminder_database"
    ).build()

    @Provides
    @Singleton
    fun provideReminderDao(database: ReminderDatabase) = database.reminderDao()


    @Provides
    @Singleton
    fun provideGeofenceUtil(@ApplicationContext context : Context) : GeofenceUtil = GeofenceUtilImpl(context)


    @Provides
    @Singleton
    fun provideNotificationUtil(@ApplicationContext context: Context) : NotificationUtil = NotificationUtilImpl(context)


    @Provides
    @Singleton
    fun provideCoroutineDispatcher() : CoroutineDispatcher = Dispatchers.IO


}
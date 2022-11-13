package com.martinz.myreminder.di

import android.app.Application
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.martinz.myreminder.data.local.ReminderDatabase
import com.martinz.myreminder.data.repository.ReminderRepositoryImpl
import com.martinz.myreminder.domain.repositoy.ReminderRepository
import com.martinz.myreminder.presentation.geofence.GeofenceUtil
import com.martinz.myreminder.presentation.geofence.GeofenceUtilImpl
import com.martinz.myreminder.presentation.notification.NotificationUtil
import com.martinz.myreminder.presentation.notification.NotificationUtilImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TestAppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth() = FirebaseAuth.getInstance()

    @Singleton
    @Provides
    fun provideDatabase(app : Application) : ReminderDatabase = Room.inMemoryDatabaseBuilder(app ,
        ReminderDatabase::class.java,
    ).build()

    @Provides
    @Singleton
    fun provideReminderDao(database: ReminderDatabase) = database.reminderDao()


    @Provides
    @ActivityRetainedScoped
    fun provideReminderRepository(
        mAuth: FirebaseAuth,
        database: ReminderDatabase,
        mGeofence : GeofenceUtil
    ) : ReminderRepository = ReminderRepositoryImpl(mAuth , database , mGeofence)


}
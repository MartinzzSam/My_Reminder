package com.martinz.myreminder.di

import android.app.Application
import android.content.Context
import androidx.navigation.Navigator
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.google.firebase.auth.FirebaseAuth
import com.martinz.myreminder.data.local.ReminderDao
import com.martinz.myreminder.data.local.ReminderDatabase
import com.martinz.myreminder.data.repository.ReminderRepositoryImpl
import com.martinz.myreminder.domain.repositoy.ReminderRepository
import com.martinz.myreminder.domain.use_cases.RegisterUseCase
import com.martinz.myreminder.domain.use_cases.reminder.DeleteReminder
import com.martinz.myreminder.domain.use_cases.reminder.ReminderUseCase
import com.martinz.myreminder.domain.use_cases.reminder.SaveReminder
import com.martinz.myreminder.domain.use_cases.validation.ValidateEmail
import com.martinz.myreminder.domain.use_cases.validation.ValidatePassword
import com.martinz.myreminder.domain.use_cases.validation.ValidateRepeatedPassword
import com.martinz.myreminder.presentation.geofence.GeofenceUtil
import com.martinz.myreminder.presentation.geofence.GeofenceUtilImpl
import com.martinz.myreminder.presentation.notification.NotificationUtil
import com.martinz.myreminder.presentation.notification.NotificationUtilImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import org.junit.Test
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object TestAppModule {

    @Provides
    @Singleton
    @Named("test_auth")
    fun provideFirebaseAuth() : FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    @Named("test_db")
    fun provideDatabase() : ReminderDatabase = Room.inMemoryDatabaseBuilder(
        ApplicationProvider.getApplicationContext() ,
        ReminderDatabase::class.java,
    ).allowMainThreadQueries().build()

    @Provides
    @Singleton
    @Named("test_dao")
    fun provideReminderDao(database: ReminderDatabase)  : ReminderDao = database.reminderDao()



    @Provides
    @Singleton
    @Named("test_geofence")
    fun provideGeofenceUtil() : GeofenceUtil = GeofenceUtilImpl(ApplicationProvider.getApplicationContext())

    @Provides
    @Singleton
    @Named("test_notification")
    fun provideNotificationUtil() : NotificationUtil = NotificationUtilImpl(ApplicationProvider.getApplicationContext())






    @Provides
    @Singleton
    @Named("test_repo")
    fun provideReminderRepository(
        @Named("test_auth")
        mAuth: FirebaseAuth,
        @Named("test_db")
        database: ReminderDatabase,
        @Named("test_geofence")
        mGeofence : GeofenceUtil
    ) : ReminderRepository = ReminderRepositoryImpl(mAuth , database , mGeofence)


    @Provides
    @Singleton
    @Named("test_reminder_use_case")
    fun provideReminderUseCase(
        @Named("test_repo")
        repository: ReminderRepository,
        @Named("test_geofence")
        geofenceUtil: GeofenceUtil
    ) : ReminderUseCase = ReminderUseCase(
        SaveReminder = SaveReminder(repository,geofenceUtil),
        DeleteReminder = DeleteReminder(repository,geofenceUtil)
    )




}
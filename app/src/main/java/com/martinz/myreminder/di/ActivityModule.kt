package com.martinz.myreminder.di

import com.google.firebase.auth.FirebaseAuth
import com.martinz.myreminder.data.local.ReminderDatabase
import com.martinz.myreminder.data.repository.ReminderRepositoryImpl
import com.martinz.myreminder.domain.repositoy.ReminderRepository
import com.martinz.myreminder.presentation.geofence.GeofenceUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
object ActivityModule {


    @Provides
    @ActivityRetainedScoped
    fun provideReminderRepository(
        mAuth: FirebaseAuth,
        database: ReminderDatabase,
        mGeofence : GeofenceUtil
    ) : ReminderRepository = ReminderRepositoryImpl(mAuth , database , mGeofence)

}
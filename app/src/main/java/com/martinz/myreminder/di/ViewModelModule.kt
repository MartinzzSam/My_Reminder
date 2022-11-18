package com.martinz.myreminder.di

import com.martinz.myreminder.domain.repositoy.ReminderRepository
import com.martinz.myreminder.domain.use_cases.RegisterUseCase
import com.martinz.myreminder.domain.use_cases.reminder.DeleteReminder
import com.martinz.myreminder.domain.use_cases.reminder.ReminderUseCase
import com.martinz.myreminder.domain.use_cases.reminder.SaveReminder
import com.martinz.myreminder.domain.use_cases.validation.ValidateEmail
import com.martinz.myreminder.domain.use_cases.validation.ValidatePassword
import com.martinz.myreminder.domain.use_cases.validation.ValidateRepeatedPassword
import com.martinz.myreminder.presentation.geofence.GeofenceUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {



    @Provides
    @ViewModelScoped
    fun provideAuthUseCase() = RegisterUseCase(
        ValidateEmail = ValidateEmail(),
        ValidatePassword = ValidatePassword(),
        ValidateRepeatedPassword = ValidateRepeatedPassword(),
    )

    @Provides
    @ViewModelScoped
    fun provideReminderUseCase(
        repository: ReminderRepository,
        geofenceUtil: GeofenceUtil
    ) = ReminderUseCase(
        SaveReminder = SaveReminder(repository , geofenceUtil),
        DeleteReminder = DeleteReminder(repository , geofenceUtil)

    )

}
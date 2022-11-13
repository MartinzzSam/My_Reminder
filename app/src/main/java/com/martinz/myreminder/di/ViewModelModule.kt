package com.martinz.myreminder.di

import com.martinz.myreminder.domain.repositoy.ReminderRepository
import com.martinz.myreminder.domain.use_cases.RegisterUseCase
import com.martinz.myreminder.domain.use_cases.validation.ValidateEmail
import com.martinz.myreminder.domain.use_cases.validation.ValidatePassword
import com.martinz.myreminder.domain.use_cases.validation.ValidateRepeatedPassword
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
    fun provideAuthUseCase(
        repository: ReminderRepository
    ) = RegisterUseCase(
        ValidateEmail = ValidateEmail(),
        ValidatePassword = ValidatePassword(),
        ValidateRepeatedPassword = ValidateRepeatedPassword(),
    )

}
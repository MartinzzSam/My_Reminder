package com.martinz.myreminder.domain.use_cases


import com.martinz.myreminder.domain.use_cases.validation.ValidateEmail
import com.martinz.myreminder.domain.use_cases.validation.ValidatePassword
import com.martinz.myreminder.domain.use_cases.validation.ValidateRepeatedPassword

data class RegisterUseCase (
    val ValidateEmail : ValidateEmail,
    val ValidatePassword : ValidatePassword,
    val ValidateRepeatedPassword : ValidateRepeatedPassword,
)
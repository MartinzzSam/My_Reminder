package com.martinz.myreminder.presentation.register_screen


import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

data class RegistrationFormState(
    val email: String = "",
    val emailError: String? = null,
    val emailErrorColor: Int = Color.Transparent.toArgb(),
    val password: String = "",
    val passwordError: String? = null,
    val passwordErrorColor: Int  = Color.Transparent.toArgb(),
    val repeatedPassword: String = "",
    val repeatedPasswordError: String? = null,
    val repeatedPasswordErrorColor: Int = Color.Transparent.toArgb(),
    val acceptedTerms: Boolean = false,
    val termsError: String? = null,

    )
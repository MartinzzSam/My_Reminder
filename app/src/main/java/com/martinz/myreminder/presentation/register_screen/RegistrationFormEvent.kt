package com.martinz.myreminder.presentation.register_screen

sealed class RegistrationFormEvent{
    data class EmailChanged(val email: String) : RegistrationFormEvent()
    data class PasswordChanged(val password: String) : RegistrationFormEvent()
    data class RepeatedPasswordChanged(
        val repeatedPassword: String
    ) : RegistrationFormEvent()

    object Submit: RegistrationFormEvent()
}
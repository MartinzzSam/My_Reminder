package com.martinz.myreminder.presentation.login_screen

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.martinz.myreminder.presentation.register_screen.RegistrationFormEvent

sealed class LoginEvent {

    data class EmailChanged(val email: String) : LoginEvent()
    data class PasswordChanged(val password: String) : LoginEvent()

    data class SignInWithGoogle(val account : GoogleSignInAccount) : LoginEvent()

    object SignInWithEmailAndPassword : LoginEvent()



}

package com.martinz.myreminder.presentation.login_screen

import com.google.android.gms.auth.api.signin.GoogleSignInAccount

sealed class LoginEvent {


    data class SignInWithGoogle(val account : GoogleSignInAccount) : LoginEvent()

    object SignInWithEmailAndPassword : LoginEvent()



}

package com.martinz.myreminder.presentation.login_screen

data class LoginState(
    val emailState : String = "",
    val passwordState : String = "",
    val isLoading : Boolean = false
)

package com.martinz.myreminder.presentation.register_screen

sealed class ValidationEvent {
        object Success: ValidationEvent()
        object Failed : ValidationEvent()
    }
package com.martinz.myreminder.core.util

import androidx.compose.ui.graphics.Color

data class ValidationResult(
    val successful: Boolean,
    val errorMessage: String? = null,
    val errorColor : Color = Color.White
)

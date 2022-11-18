package com.martinz.myreminder.core.util



data class ValidationResult(
    val successful: Boolean,
    val errorMessage: String? = null,
)

package com.martinz.myreminder.domain.use_cases.validation



import androidx.compose.ui.graphics.Color
import com.martinz.myreminder.core.util.ValidationResult


class ValidateRepeatedPassword {
    operator fun invoke(password: String, repeatedPassword: String): ValidationResult {
        if(password != repeatedPassword) {
            return ValidationResult(
                successful = false,
                errorMessage = "The passwords don't match",
                errorColor = Color.Red
            )
        }
        return ValidationResult(
            successful = true
        )
    }
}
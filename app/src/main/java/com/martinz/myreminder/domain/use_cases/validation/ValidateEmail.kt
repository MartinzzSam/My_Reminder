package com.martinz.myreminder.domain.use_cases.validation


import android.util.Patterns
import androidx.compose.ui.graphics.Color
import com.martinz.myreminder.core.util.ValidationResult


class ValidateEmail {
    val EMAIL_REGEX = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})";

    operator fun invoke(email: String): ValidationResult {
        if(email.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "The email can't be blank",
                errorColor = Color.Red
            )
        }
        // IDK really why this not working with testing
//        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
        if(!EMAIL_REGEX.toRegex().matches(email)) {
            return ValidationResult(
                successful = false,
                errorMessage = "That's not a valid email",
                errorColor = Color.Red,
            )
        }
        return ValidationResult(
            successful = true
        )
    }
}
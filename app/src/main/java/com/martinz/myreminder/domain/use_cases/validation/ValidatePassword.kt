package com.martinz.myreminder.domain.use_cases.validation


import com.martinz.myreminder.core.util.ValidationResult


class ValidatePassword {
    operator fun invoke(password: String): ValidationResult {
        if(password.length < 8) {
            return ValidationResult(
                successful = false,
                errorMessage = "The password needs to consist of at least 8 characters",
            )
        }
        val containsLettersAndDigits = password.any { it.isDigit() } &&
                password.any { it.isLetter() }
        if(!containsLettersAndDigits) {
            return ValidationResult(
                successful = false,
                errorMessage = "The password needs to contain at least one letter and digit",
            )
        }

        val characterCheck = password.toCharArray()
        characterCheck.forEach { char->
            if (Character.UnicodeBlock.of(char) == Character.UnicodeBlock.ARABIC) {
                return ValidationResult(
                    successful = false,
                    errorMessage = "The password needs to contain english letters and numbers only",
                )
            }
        }
        return ValidationResult(
            successful = true
        )
    }
}
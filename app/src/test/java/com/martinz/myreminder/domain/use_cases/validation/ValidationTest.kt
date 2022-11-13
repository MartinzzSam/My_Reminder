package com.martinz.myreminder.domain.use_cases.validation


import androidx.arch.core.executor.TaskExecutor
import androidx.test.filters.SmallTest
import androidx.test.runner.AndroidJUnit4
import androidx.test.runner.AndroidJUnitRunner
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4


 internal class ValidationTest {

    @Test
    fun `ValidateEmail() return ValidationResult() with successful equal true if email is valid`() {
        val email = "email@gmail.com"
        val result = ValidateEmail().invoke(email)

        assertTrue(result.successful)
    }

     @Test
     fun `ValidatePassword() return ValidationResult() with successful equal true if Password is valid`() {
         val password = "password1234"
         val result = ValidatePassword().invoke(password)
         assertTrue(result.successful)
     }

     @Test
     fun `ValidateRepeatedPassword() return ValidationResult() with successful equal true if two Passwords match is valid`() {
         val password = "password1234"
         val repeatedPassword = "password1234"
         val result = ValidateRepeatedPassword().invoke(password, repeatedPassword)
         assertTrue(result.successful)
     }

}

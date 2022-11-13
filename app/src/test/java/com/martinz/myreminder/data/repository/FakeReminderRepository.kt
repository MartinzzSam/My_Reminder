package com.martinz.myreminder.data.repository

import androidx.compose.runtime.mutableStateOf
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.martinz.myreminder.core.util.Response
import com.martinz.myreminder.domain.model.Reminder
import com.martinz.myreminder.domain.repositoy.ReminderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.junit.Assert.assertTrue
import org.junit.Test
import java.lang.AssertionError
import kotlin.math.log

class FakeReminderRepository : ReminderRepository {


    private val reminders = mutableListOf<Reminder>()

    private val loginStatus = mutableStateOf(true)


    private fun  setLoginStatus(value : Boolean) {
        loginStatus.value = value
    }


    override fun getUserLoginStatus() : Boolean{
     return loginStatus.value
    }

    override fun signOut() {
        setLoginStatus(false)
    }

    override suspend fun emailAndPasswordLogin(
        email: String,
        password: String
    ): Flow<Response<String>> {
        TODO("Not yet implemented")
    }

    override suspend fun registerWithEmailAndPassword(
        email: String,
        password: String
    ): Flow<Response<String>> {
        TODO("Not yet implemented")
    }

    override suspend fun signWithGoogle(account: GoogleSignInAccount): Flow<Response<String>> {
        TODO("Not yet implemented")
    }

    override suspend fun saveReminder(reminder: Reminder): Flow<Response<String>> {
        return flow {
            val save = reminders.add(reminder)
            if (save) {
                emit(Response.Success("reminder saved successfully"))
            } else {
                emit(Response.Error("could not save reminder"))
            }
        }
    }

    override suspend fun deleteReminder(reminder: Reminder): Flow<Response<String>> {
         return flow {
             val delete = reminders.remove(reminder)
             if (delete) {
                 emit(Response.Success("reminder deleted"))
             } else {
                 emit(Response.Error("failed to delete reminder"))
             }
         }
    }

    override fun getAllReminders(): Flow<List<Reminder>> {
        return flow { emit(reminders) }
    }

    override suspend fun getReminderById(reminderId: Int): Reminder? {
       return reminders.find { it.id == reminderId }
    }
//
//    @Test
//    fun `test login status return true if login status is true`() {
//        assertTrue(getUserLoginStatus())
//    }

//    @Test
//    fun  `test signOut()`() {
//        signOut()
//        assertTrue(!loginStatus.value)
//    }
//
//    @Test
//    fun `test signInWithGoogle()`() {
//
//    }


}
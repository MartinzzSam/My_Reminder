package com.martinz.myreminder.data.repository


import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.martinz.myreminder.core.util.Response
import com.martinz.myreminder.domain.model.Reminder
import com.martinz.myreminder.domain.repositoy.ReminderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class FakeReminderRepository : ReminderRepository {


    private val reminders = mutableListOf<Reminder>()


    private var shouldReturnError = false
    private var loginStatus = false

    fun setShouldReturnError(shouldReturn: Boolean) {
        this.shouldReturnError = shouldReturn
    }


    private fun  setLoginStatus(value : Boolean) {
        loginStatus  = value
    }


    override fun getUserLoginStatus() : Boolean{
     return loginStatus
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

    override suspend fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): Flow<Response<String>> {
        TODO("Not yet implemented")
    }

    override suspend fun addReminder(reminder: Reminder)  {reminders.add(reminder)}

    override suspend fun deleteReminder(reminder: Reminder) { reminders.remove(reminder)}

    override fun getAllReminders(): Flow<List<Reminder>> {
        return flow { emit(reminders) }
    }

    override suspend fun getReminderById(reminderId: Int): Reminder? {
       return reminders.find { it.id == reminderId }
    }



}
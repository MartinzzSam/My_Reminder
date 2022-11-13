package com.martinz.myreminder.domain.repositoy

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.martinz.myreminder.core.util.Response
import com.martinz.myreminder.domain.model.Reminder
import kotlinx.coroutines.flow.Flow

interface ReminderRepository {


    fun  getUserLoginStatus() : Boolean


    fun signOut()

    suspend fun emailAndPasswordLogin(email : String , password : String) : Flow<Response<String>>


    suspend fun registerWithEmailAndPassword(email: String , password: String) : Flow<Response<String>>


    suspend fun signWithGoogle(account : GoogleSignInAccount) : Flow<Response<String>>


    suspend fun signInWithEmailAndPassword(email: String , password: String) : Flow<Response<String>>


    suspend fun saveReminder(reminder: Reminder) : Flow<Response<String>>

    suspend fun deleteReminder(reminder: Reminder) : Flow<Response<String>>

    fun getAllReminders() : Flow<List<Reminder>>

    suspend fun getReminderById(reminderId: Int) : Reminder?
}
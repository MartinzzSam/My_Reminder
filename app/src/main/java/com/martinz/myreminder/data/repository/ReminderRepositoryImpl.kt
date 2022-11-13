package com.martinz.myreminder.data.repository

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.martinz.myreminder.core.myLog
import com.martinz.myreminder.core.util.Response
import com.martinz.myreminder.data.local.ReminderDatabase
import com.martinz.myreminder.domain.model.Reminder
import com.martinz.myreminder.domain.repositoy.ReminderRepository
import com.martinz.myreminder.presentation.geofence.GeofenceUtil
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await

class ReminderRepositoryImpl(
    private val mAuth: FirebaseAuth,
    private val database: ReminderDatabase,
    private val mGeofence: GeofenceUtil
) : ReminderRepository {


    private val reminderDao = database.reminderDao()


    override fun getUserLoginStatus(): Boolean {
        val user = mAuth.currentUser

        if (user != null) {
            return true
        }

        return false

    }

    override fun signOut() {
        mAuth.signOut()
    }


    override suspend fun emailAndPasswordLogin(
        email: String,
        password: String
    ): Flow<Response<String>> = flow {

        try {

            mAuth.signInWithEmailAndPassword(email, password).await()

            emit(Response.Success("Login Successfully"))
            return@flow


        } catch (e: ApiException) {
            emit(Response.Error(e.message.toString()))
            return@flow
        }


    }

    override suspend fun registerWithEmailAndPassword(
        email: String,
        password: String
    ): Flow<Response<String>> = flow {
        try {

            mAuth.createUserWithEmailAndPassword(email, password).await()

            emit(Response.Success("Login Successfully"))
            return@flow


        } catch (e: Exception) {
            emit(Response.Error(e.message.toString()))
            return@flow
        }
    }

    override suspend fun signWithGoogle(account: GoogleSignInAccount): Flow<Response<String>> =
        flow {
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)

            try {
                mAuth.signInWithCredential(credential).await()
                emit(Response.Success("User Logged In Successfully"))
                return@flow

            } catch (e: Throwable) {

                emit(Response.Error(e.toString()))
                return@flow
            }


        }

    override suspend fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): Flow<Response<String>> = flow {
        try {
             mAuth.signInWithEmailAndPassword(email, password).await()
            emit(Response.Success("User Logged In Successfully"))
            return@flow

        } catch (e: Exception) {
            emit(Response.Error(e.message.toString()))
            return@flow
        }


    }


    override suspend fun saveReminder(reminder: Reminder): Flow<Response<String>> = flow {
        try {
            val geoFence = mGeofence.addGeofence(reminder).catch {
                myLog("FLow catch : $this")
            }.first()
            when (geoFence) {
                is Response.Success -> {

                    reminderDao.addReminder(reminder)
                    emit(Response.Success("Added Successfully"))

                }

                is Response.Error -> {
                    emit(Response.Error(geoFence.message))

                }
            }


        } catch (e: Exception) {
            emit(Response.Error(e.message.toString()))
        }
    }

    override suspend fun deleteReminder(reminder: Reminder): Flow<Response<String>> =
        mGeofence.deleteGeofence(reminder.id.toString()).map { response ->
            when (response) {
                is Response.Success -> {
                    reminderDao.deleteReminder(reminder)
                    Response.Success(response.data)
                }

                is Response.Error -> {
                    Response.Error(response.message)
                }
            }
        }


    override fun getAllReminders(): Flow<List<Reminder>> = reminderDao.getAllReminders()


    override suspend fun getReminderById(reminderId: Int): Reminder =
        reminderDao.getReminderById(reminderId)


}
package com.martinz.myreminder.presentation.geofence

import com.google.android.gms.location.Geofence
import com.martinz.myreminder.core.util.Response
import com.martinz.myreminder.domain.model.Reminder
import kotlinx.coroutines.flow.Flow

interface GeofenceUtil {



    suspend fun addGeofence(reminder: Reminder) : Flow<Response<String>>

    suspend fun deleteGeofence(reminderId: String) : Flow<Response<String>>
}
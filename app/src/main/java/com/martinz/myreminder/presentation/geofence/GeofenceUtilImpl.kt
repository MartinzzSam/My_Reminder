package com.martinz.myreminder.presentation.geofence

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.martinz.myreminder.core.myLog
import com.martinz.myreminder.core.util.Response
import com.martinz.myreminder.domain.model.Reminder
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await


class GeofenceUtilImpl(val context: Context) : GeofenceUtil {

    private val geofencingClient = LocationServices.getGeofencingClient(context)


    private val geofencePendingIntent: PendingIntent by lazy {
        val intent = Intent(context, GeofenceBroadcastReceiver::class.java)

        PendingIntent.getBroadcast(context, 200, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }


    @SuppressLint("MissingPermission")
    override suspend fun addGeofence(reminder: Reminder): Flow<Response<String>> = flow {
//      if (ActivityCompat.checkSelfPermission(
//            context, Manifest.permission.ACCESS_FINE_LOCATION
//         ) != PackageManager.PERMISSION_GRANTED) {

        try {
            val geofence = buildGeofence(reminder)
            val geofenceRequest = getGeofencingRequest(geofence)
            geofencingClient.addGeofences(geofenceRequest, geofencePendingIntent)
                .addOnSuccessListener {
                    myLog("Geofence Success")
                }.addOnCompleteListener {
                myLog("Geofence Completed")
            }.addOnCanceledListener {
                myLog("Geofence Canceled")
            }.addOnFailureListener {
                myLog("Geofence Failure  $it")
            }.await().runCatching {
                emit(Response.Success("Geofence Added Successfully"))
            }


        } catch (e: Throwable) {
            myLog(e.stackTrace.toString())
            emit(Response.Error("Failed To Add Geofence"))
        }

//      } else {
//         emit(Response.Error("Geofence Needs Location Permission"))
//      }
    }

    @SuppressLint("MissingPermission")
    override suspend fun deleteGeofence(reminderId: String): Flow<Response<String>> = flow {
//      if (ActivityCompat.checkSelfPermission(
//            context, Manifest.permission.ACCESS_FINE_LOCATION
//         ) != PackageManager.PERMISSION_GRANTED) {

        try {
          geofencingClient.removeGeofences(listOf(reminderId)).await()
            emit(Response.Success("Geofence Deleted Successfully"))

        } catch (e: Exception) {
            emit(Response.Error("Failed To Add Geofence"))
        }


//      } else {
//         emit(Response.Error("Geofence Needs Location Permission"))
//
//      }
    }


    private fun getGeofencingRequest(geofence: Geofence): GeofencingRequest {
        return GeofencingRequest.Builder().apply {
            setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            addGeofence(geofence)
        }.build()
    }


    private fun buildGeofence(reminder: Reminder): Geofence {
        return Geofence.Builder()
            .setCircularRegion(
                reminder.latLong.latitude,
                reminder.latLong.longitude,
                200.00f
            ).setExpirationDuration(Geofence.NEVER_EXPIRE)
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)
            .setRequestId(reminder.id.toString())
            .build()
    }


}
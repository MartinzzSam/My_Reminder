package com.martinz.myreminder.presentation.geofence

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingEvent
import com.martinz.myreminder.core.myLog
import com.martinz.myreminder.data.local.ReminderDao
import com.martinz.myreminder.data.local.ReminderDatabase
import com.martinz.myreminder.domain.repositoy.ReminderRepository
import com.martinz.myreminder.presentation.notification.NotificationUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class GeofenceBroadcastReceiver : BroadcastReceiver() {


    @Inject
    lateinit var notificationUtil: NotificationUtil

    @Inject
    lateinit var reminderDao: ReminderDao




    override fun onReceive(context: Context?, intent: Intent?) {


        if (intent != null) {
            val geofencingEvent = GeofencingEvent.fromIntent(intent)
            if (geofencingEvent?.hasError() == true) {
                val errorMessage = GeofenceStatusCodes
                    .getStatusCodeString(geofencingEvent.errorCode)
                myLog(errorMessage)
                return
            }


            val triggeringGeofence = geofencingEvent?.triggeringGeofences
            when(geofencingEvent?.geofenceTransition) {

                Geofence.GEOFENCE_TRANSITION_ENTER -> {
                   triggeringGeofence?.forEach {geofence ->
                        CoroutineScope(Dispatchers.IO).launch {
                            val reminder =  reminderDao.getReminderById(geofence.requestId.toInt())
                            notificationUtil.createNotification( reminder.id , reminder.title, reminder.description)
                        }
                    }
                    myLog("Receiver Enter Event : ${triggeringGeofence.toString()}")
                }


                Geofence.GEOFENCE_TRANSITION_EXIT -> {
                    triggeringGeofence?.forEach { geofence ->
                        CoroutineScope(Dispatchers.IO).launch {
                            val reminder =  reminderDao.getReminderById(geofence.requestId.toInt())
                            notificationUtil.dismissNotification(reminder.id)
                        }
                    }

                    myLog("Receiver Exit Event : ${triggeringGeofence.toString()}")

                }
            }

        } else {
            myLog("Null Intent")
        }

    }


}

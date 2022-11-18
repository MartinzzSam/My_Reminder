package com.martinz.myreminder.domain.use_cases.reminder

import com.martinz.myreminder.core.util.Response
import com.martinz.myreminder.domain.model.Reminder
import com.martinz.myreminder.domain.repositoy.ReminderRepository
import com.martinz.myreminder.presentation.geofence.GeofenceUtil
import kotlinx.coroutines.flow.map

class SaveReminder(
    private val repository: ReminderRepository,
    private val geofence: GeofenceUtil
) {

    suspend operator fun invoke(reminder: Reminder) = geofence.addGeofence(reminder).map { geoFence ->
        when (geoFence) {
            is Response.Success -> {
                repository.addReminder(reminder)
                Response.Success("Added Successfully")

            }

            is Response.Error -> {
                Response.Error(geoFence.message)

            }
        }

    }
}
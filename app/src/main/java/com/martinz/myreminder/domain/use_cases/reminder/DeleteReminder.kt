package com.martinz.myreminder.domain.use_cases.reminder

import com.martinz.myreminder.core.util.Response
import com.martinz.myreminder.domain.model.Reminder
import com.martinz.myreminder.domain.repositoy.ReminderRepository
import com.martinz.myreminder.presentation.geofence.GeofenceUtil
import kotlinx.coroutines.flow.map

class DeleteReminder(
    private val repository: ReminderRepository,
    private val geofence: GeofenceUtil
) {


    suspend operator fun invoke(reminder: Reminder) =
        geofence.deleteGeofence(reminder.id.toString()).map { response ->
            when (response) {
                is Response.Success -> {
                    repository.deleteReminder(reminder)
                    Response.Success(response.data)
                }

                is Response.Error -> {
                    Response.Error(response.message)
                }
            }
        }

}

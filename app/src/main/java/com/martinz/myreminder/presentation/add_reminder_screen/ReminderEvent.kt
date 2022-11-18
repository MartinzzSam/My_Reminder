package com.martinz.myreminder.presentation.add_reminder_screen

import com.google.android.gms.maps.model.LatLng
import com.martinz.myreminder.domain.model.Reminder

sealed class ReminderEvent {

    object SaveReminder : ReminderEvent()
    data class DeleteReminder(val reminder: Reminder) : ReminderEvent()
    data class TitleChanged(val title : String) : ReminderEvent()
    data class DescriptionChanged(val description : String) : ReminderEvent()
    data class SliderChanged(val range : Float) : ReminderEvent()
    data class LocationChanged(val latLong: LatLng) : ReminderEvent()

}

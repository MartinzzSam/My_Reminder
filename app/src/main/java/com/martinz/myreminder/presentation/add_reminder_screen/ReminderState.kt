package com.martinz.myreminder.presentation.add_reminder_screen

import com.google.android.gms.maps.model.LatLng
import org.junit.runner.Description

data class ReminderState(
    val title : String = "",
    val description: String = "",
    val range : Float = 100f,
    val location : LatLng? = null
)

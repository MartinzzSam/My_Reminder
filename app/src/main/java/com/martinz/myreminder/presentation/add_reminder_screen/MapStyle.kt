package com.martinz.myreminder.presentation.add_reminder_screen

import android.graphics.Color
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.MapStyleOptions

sealed interface MapStyle {


    object SilverMap : MapStyle

    object DarkMap : MapStyle

}

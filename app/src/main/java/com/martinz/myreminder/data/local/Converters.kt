package com.martinz.myreminder.data.local

import androidx.room.TypeConverter
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson

class Converters {

    @TypeConverter
    fun toLatLng(locationString: String?): LatLng? {
        return try {
            Gson().fromJson(locationString, LatLng::class.java)
        } catch (e: Exception) {
            null
        }
    }

    @TypeConverter
    fun toLatLngString(location: LatLng?): String? {
        return Gson().toJson(location)
    }

}
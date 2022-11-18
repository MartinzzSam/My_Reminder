package com.martinz.myreminder.domain.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng
import kotlinx.parcelize.Parcelize
import java.io.Serializable
import kotlin.random.Random


@Entity(tableName = "location_reminder")
data class Reminder(
    @PrimaryKey(autoGenerate = false)
    val id : Int = Random.nextInt(),
    val title : String,
    val description : String,
    val latLong : LatLng,
    val range : Float
    ) : Serializable

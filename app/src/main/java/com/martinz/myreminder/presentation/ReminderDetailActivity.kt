package com.martinz.myreminder.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.ColorUtils
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import com.martinz.myreminder.R
import com.martinz.myreminder.databinding.ActivityReminderDetailBinding
import com.martinz.myreminder.domain.model.Reminder
import kotlinx.coroutines.runBlocking


class ReminderDetailActivity : AppCompatActivity() {

    lateinit var mReminder: Reminder
    private var _binding: ActivityReminderDetailBinding? = null
    private val binding get() = _binding!!
    private var mMap: MapView? = null


    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityReminderDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        receiveIntent()

        loadData()

        mMap = binding.mapView

        mMap!!.onCreate(savedInstanceState).run {

            mMap!!.getMapAsync { googleMap ->


                googleMap.isMyLocationEnabled = true
                googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this@ReminderDetailActivity,  R.raw.dark_map))
                val markerOptions = MarkerOptions()
                val circleOptions = CircleOptions()



                markerOptions.position(mReminder.latLong)


                markerOptions.title(mReminder.latLong.latitude.toString() + ":" + mReminder.latLong.longitude)
                circleOptions.center(mReminder.latLong)
                    .fillColor(
                        ColorUtils.setAlphaComponent(
                            this@ReminderDetailActivity.getColor(R.color.outer_space),
                            100
                        )
                    )
                    .strokeColor(this@ReminderDetailActivity.getColor(R.color.white))
                    .strokeWidth(2f)
                    .radius(mReminder.range.toDouble())


                googleMap.addMarker(markerOptions)
                googleMap.addCircle(circleOptions)
                googleMap.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        mReminder.latLong, 15F
                    )
                )

            }


        }






    }

    override fun onStart() {
        super.onStart()
        mMap?.onStart()
    }

    override fun onStop() {
        super.onStop()
        mMap?.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mMap?.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mMap?.onDestroy()
    }

    override fun onPause() {
        super.onPause()
        mMap?.onPause()
    }



    private fun loadData() {
        binding.apply {
            tvTitle.text = mReminder.title
            tvDesc.text = mReminder.description
        }
    }


    private fun receiveIntent() {
        intent?.extras?.let {
            val reminder = intent.getStringExtra("reminder")
            val json = Gson().fromJson(reminder , Reminder::class.java)
            mReminder = json
        }
    }




}
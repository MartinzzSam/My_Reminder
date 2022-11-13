package com.martinz.myreminder.presentation.add_reminder_screen

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.martinz.myreminder.R
import com.martinz.myreminder.core.changeStatusBarColor
import com.martinz.myreminder.core.myLog
import com.martinz.myreminder.databinding.FragmentReminderBinding
import com.martinz.myreminder.domain.model.Reminder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReminderFragment : Fragment() {

    private var _binding: FragmentReminderBinding? = null
    private val binding get() = _binding!!
    private var mMap: MapView? = null
    private var currentLocation : LatLng? = null
    private val reminderViewModel : ReminderViewModel by viewModels()


    override fun onAttach(context: Context) {
        super.onAttach(context)
        changeStatusBarColor(R.color.vista_blue)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentReminderBinding.inflate(layoutInflater)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mMap = binding.mapView

        mMap!!.onCreate(savedInstanceState).run {
            mMap!!.getMapAsync { googleMap ->

                googleMap.setOnMapClickListener { latLng ->
                    val markerOptions = MarkerOptions()
                    val circleOptions = CircleOptions()

                    circleOptions.center(latLng)
                        .fillColor(Color.Red.copy(alpha = 0.5f).toArgb())
                        .strokeWidth(2f)
                        .strokeColor(Color.White.toArgb())
                        .radius(200.00)

                    markerOptions.position(latLng)


                    markerOptions.title(latLng.latitude.toString() + ":" + latLng.longitude)

                    googleMap.clear()

                    googleMap.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            latLng, 15F
                        )
                    )

                    currentLocation = latLng
                    googleMap.addMarker(markerOptions)
                    googleMap.addCircle(circleOptions)

                }


            }
        }

        onClicks()


    }


    private fun onClicks() {
        binding.apply {
            saveBtn.setOnClickListener {
                if (currentLocation != null) {
                    reminderViewModel.onEvent(ReminderEvent.SaveReminder(Reminder(
                        title = etTitle.text.toString(),
                        description = etDesc.text.toString(),
                        latLong = currentLocation!!
                    )))
                } else {
                    myLog("Please Select Location")
                }
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


}
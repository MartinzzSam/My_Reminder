package com.martinz.myreminder.presentation.add_reminder_screen

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.graphics.ColorUtils
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.martinz.myreminder.R
import com.martinz.myreminder.core.base.BaseFragment
import com.martinz.myreminder.core.uiEventObserver
import com.martinz.myreminder.core.util.ReminderTextWatcher
import com.martinz.myreminder.databinding.FragmentMainBinding
import com.martinz.myreminder.databinding.FragmentReminderBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ReminderFragment(viewModelTestFragment: ReminderViewModel? = null) :
    BaseFragment<FragmentReminderBinding, ReminderViewModel>(
        ReminderViewModel::class.java, viewModelTestFragment
    ) {


    lateinit var mGoogleMap: GoogleMap
    private var mMap: MapView? = null
    private var range: Float = 100f


    override fun getViewBinding() = FragmentReminderBinding.inflate(layoutInflater)


    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        uiEventObserver(viewModel.uiEvent)


        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                stateObserver()
            }
        }

        mMap = binding.mapView

        mMap!!.onCreate(savedInstanceState).run {

            mMap!!.getMapAsync { googleMap ->

                mGoogleMap = googleMap

                googleMap.isMyLocationEnabled = true
                setupMapStyle(requireContext(), googleMap, MapStyle.SilverMap)

                googleMap.setOnMapClickListener { latLng ->
                    val markerOptions = MarkerOptions()
                    val circleOptions = CircleOptions()

                    circleOptions.center(latLng)
                        .fillColor(
                            ColorUtils.setAlphaComponent(
                                requireContext().getColor(R.color.outer_space),
                                100
                            )
                        )
                        .strokeColor(requireContext().getColor(R.color.white))
                        .strokeWidth(2f)
                        .radius(range.toDouble())



                    markerOptions.position(latLng)


                    markerOptions.title(latLng.latitude.toString() + ":" + latLng.longitude)

                    googleMap.clear()

                    googleMap.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            latLng, 15F
                        )
                    )

                    viewModel.onEvent(ReminderEvent.LocationChanged(latLng))
                    googleMap.addMarker(markerOptions)
                    googleMap.addCircle(circleOptions)

                }


            }


        }

        onClicks()


    }


    private fun onClicks() {
        binding.apply {
            saveBtn.setOnClickListener { viewModel.onEvent(ReminderEvent.SaveReminder) }
            etTitle.addTextChangedListener(ReminderTextWatcher {
                viewModel.onEvent(
                    ReminderEvent.TitleChanged(it)
                )
            })
            etDesc.addTextChangedListener(ReminderTextWatcher {
                viewModel.onEvent(
                    ReminderEvent.DescriptionChanged(it)
                )
            })
            rangeSlider.addOnChangeListener { slider, value, fromUser ->
                viewModel.onEvent(
                    ReminderEvent.SliderChanged(value)
                )
            }
            mapStyleSwitch.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    setupMapStyle(requireContext(), mGoogleMap, MapStyle.DarkMap)
                    mMap!!.refreshDrawableState()
                } else {
                    setupMapStyle(requireContext(), mGoogleMap, MapStyle.SilverMap)
                    mMap!!.refreshDrawableState()
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


    private suspend fun stateObserver() {
        viewModel.state.collect {
            range = it.range
        }
    }


    private fun setupMapStyle(context: Context, googleMap: GoogleMap, mapStyle: MapStyle) {

        when (mapStyle) {


            is MapStyle.SilverMap -> {
                googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                        context,
                        R.raw.silver_map
                    )
                )
            }


            is MapStyle.DarkMap -> {
                googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(context, R.raw.dark_map))
            }
        }
    }


}
package com.martinz.myreminder.presentation.map_screen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions
import com.martinz.myreminder.R
import com.martinz.myreminder.databinding.FragmentMainBinding
import com.martinz.myreminder.databinding.FragmentMapBinding


class MapFragment : Fragment() {


    private lateinit var supportMapFragment: SupportMapFragment
    private var _binding : FragmentMapBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMapBinding.inflate(layoutInflater)
        supportMapFragment = childFragmentManager.findFragmentById(R.id.google_map) as SupportMapFragment

        supportMapFragment.getMapAsync(){ googleMap ->


            googleMap.setOnMapClickListener { latLng ->
                val markerOptions = MarkerOptions()

                markerOptions.position(latLng)

                markerOptions.title(latLng.latitude.toString() + ":" + latLng.longitude)

                googleMap.clear()

                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    latLng, 10F
                ))

                googleMap.addMarker(markerOptions)
            }


        }

        return binding.root
    }










}
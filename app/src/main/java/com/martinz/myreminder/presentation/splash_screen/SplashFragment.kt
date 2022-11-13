package com.martinz.myreminder.presentation.splash_screen

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.martinz.myreminder.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import java.security.Permission
import javax.inject.Inject

@AndroidEntryPoint
class SplashFragment : Fragment() {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerActivityForResult().launch(arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION,Manifest.permission.ACCESS_BACKGROUND_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION))


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }


    override fun onStart() {
        super.onStart()
        CoroutineScope(Dispatchers.Main).launch {
            delay(1000L)
            checkUserLogInStatus()
        }
    }





    private fun checkUserLogInStatus() {
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToMainFragment())
        } else {
            findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToLoginFragment())
        }
    }

    private  fun  registerActivityForResult() =  registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions())
    { permissions ->
        permissions.entries.forEach {
            val permissionName = it.key
            val isGranted = it.value
            if (isGranted) {

            } else {
                Toast.makeText(this.context, permissionName , Toast.LENGTH_SHORT).show()
                requireActivity().finish()
            }
        }
    }

}
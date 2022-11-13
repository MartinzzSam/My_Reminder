package com.martinz.myreminder.core

import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.martinz.myreminder.core.util.UiEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest

fun myLog(message : String) {
    Log.e("myOwnLogs" , message)
}

 suspend fun Fragment.uiEventObserver(uiEvent : Flow<UiEvent?>) {
    uiEvent.collectLatest { event ->
        when(event) {
            is UiEvent.ShowSnackBar -> {
                Toast.makeText(this.context , event.message , Toast.LENGTH_SHORT).show()
            }

            is UiEvent.Navigate -> {
                findNavController().navigate(event.directions)
            }

            else -> {}
        }

    }
}

fun Fragment.changeStatusBarColor(color : Int) {
    activity?.window?.statusBarColor = color
}
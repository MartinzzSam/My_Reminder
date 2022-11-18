package com.martinz.myreminder.core

import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.martinz.myreminder.core.util.UiEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

fun myLog(message : String) {
    Log.e("myOwnLogs" , message)
}

fun Fragment.uiEventObserver(uiEvent : Flow<UiEvent?>) {
    viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            uiEvent.collect { event ->
                when(event) {
                    is UiEvent.ShowToast -> {
                        Toast.makeText(context , event.message , Toast.LENGTH_SHORT).show()
                    }

                    is UiEvent.Navigate -> {
                        findNavController().navigate(event.directions)
                    }

                    else -> {}
                }

            }
        }
    }
}

fun Fragment.changeStatusBarColor(color : Int) {
    activity?.window?.statusBarColor = color
}
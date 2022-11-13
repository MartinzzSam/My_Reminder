package com.martinz.myreminder.core.util

import androidx.navigation.NavDirections

sealed class UiEvent {

    data class ShowSnackBar(val message : String ) : UiEvent()
    data class Navigate(val directions: NavDirections) : UiEvent()
}


package com.martinz.myreminder.presentation.main_screen

import com.martinz.myreminder.domain.model.Reminder

sealed class MainEvent {


    object SignOut : MainEvent()

    data class DeleteReminder(val reminder: Reminder) : MainEvent()

}

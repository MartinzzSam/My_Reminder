package com.martinz.myreminder.presentation.add_reminder_screen

import com.martinz.myreminder.domain.model.Reminder

sealed class ReminderEvent {

    data class SaveReminder(val reminder : Reminder) : ReminderEvent()
    data class DeleteReminder(val reminder: Reminder) : ReminderEvent()

}

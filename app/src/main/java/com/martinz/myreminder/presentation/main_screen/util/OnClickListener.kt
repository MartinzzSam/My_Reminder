package com.martinz.myreminder.presentation.main_screen.util

import com.martinz.myreminder.domain.model.Reminder


class OnClickListener(val clickListener: (reminder : Reminder) -> Unit) {
    fun onClick(reminder: Reminder) = clickListener(reminder)
}
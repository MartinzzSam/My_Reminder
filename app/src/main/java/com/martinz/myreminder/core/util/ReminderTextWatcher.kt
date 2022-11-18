package com.martinz.myreminder.core.util

import android.text.Editable
import android.text.TextWatcher

class ReminderTextWatcher(val onTextChange : (String) -> Unit) : TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

    }

    override fun afterTextChanged(s: Editable?) {
       onTextChange(s.toString())
    }
}
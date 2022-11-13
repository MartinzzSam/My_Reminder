package com.martinz.myreminder.presentation.register_screen

import android.text.Editable
import android.text.TextWatcher

class RegisterTextWatcher(val onTextChange : (String) -> Unit) : TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

    }

    override fun afterTextChanged(s: Editable?) {
       onTextChange(s.toString())
    }
}
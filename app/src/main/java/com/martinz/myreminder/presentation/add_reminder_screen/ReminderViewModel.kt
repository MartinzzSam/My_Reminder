package com.martinz.myreminder.presentation.add_reminder_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.martinz.myreminder.core.myLog
import com.martinz.myreminder.core.util.Response
import com.martinz.myreminder.domain.repositoy.ReminderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ReminderViewModel @Inject constructor(
    private val repository: ReminderRepository
): ViewModel() {









    fun onEvent(event : ReminderEvent) {

        when(event) {



            is ReminderEvent.SaveReminder -> {
                viewModelScope.launch(Dispatchers.IO) {
                    repository.saveReminder(event.reminder).collect { response ->
                        when(response) {
                            is Response.Success -> {
                                myLog(response.toString())
                            }

                            is Response.Error -> {
                                myLog(response.toString())
                            }
                        }

                    }
                }

            }

            is ReminderEvent.DeleteReminder -> {
                viewModelScope.launch(Dispatchers.IO) {
                    repository.deleteReminder(event.reminder).collect { response ->
                        myLog(response.toString())
                        when(response) {

                            is Response.Success -> {
                                myLog(response.toString())
                            }

                            is Response.Error -> {
                                myLog(response.toString())
                            }
                        }

                    }
                }
            }
        }

    }
}
package com.martinz.myreminder.presentation.main_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.martinz.myreminder.core.util.Response
import com.martinz.myreminder.core.util.UiEvent
import com.martinz.myreminder.domain.model.Reminder
import com.martinz.myreminder.domain.repositoy.ReminderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository : ReminderRepository
) : ViewModel() {


    private val _uiEvent = Channel<UiEvent?>()
     val uiEvent : Flow<UiEvent?> = _uiEvent.consumeAsFlow()

    private val _reminders = MutableStateFlow<List<Reminder>?>(null)
    val reminder : StateFlow<List<Reminder>?> = _reminders.asStateFlow()


    init {
        viewModelScope.launch {
          getAllReminders()
        }
    }




    fun onEvent(event : MainEvent) {

        when(event) {

            is MainEvent.SignOut -> {
                viewModelScope.launch {
                    repository.signOut()
                    _uiEvent.send(UiEvent.Navigate(MainFragmentDirections.actionMainFragmentToLoginFragment2()))
                }

            }

            is MainEvent.DeleteReminder -> {
                viewModelScope.launch(Dispatchers.IO) {
                    repository.deleteReminder(event.reminder).collect { response ->
                        when(response) {
                            is Response.Success -> {
                                _uiEvent.send(UiEvent.ShowSnackBar(response.data))

                            }

                            is Response.Error -> {
                                _uiEvent.send(UiEvent.ShowSnackBar(response.message))
                            }
                        }
                    }

                }
            }
        }
    }


     suspend fun getAllReminders() {
        repository.getAllReminders().collect {
            _reminders.emit(it)
        }
    }



}
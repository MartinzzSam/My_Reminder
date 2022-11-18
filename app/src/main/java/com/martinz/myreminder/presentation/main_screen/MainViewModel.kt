package com.martinz.myreminder.presentation.main_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.martinz.myreminder.core.util.Response
import com.martinz.myreminder.core.util.UiEvent
import com.martinz.myreminder.domain.model.Reminder
import com.martinz.myreminder.domain.repositoy.ReminderRepository
import com.martinz.myreminder.domain.use_cases.reminder.DeleteReminder
import com.martinz.myreminder.domain.use_cases.reminder.ReminderUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository : ReminderRepository,
    private val useCase: ReminderUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {


    private val _uiEvent = Channel<UiEvent?>()
     val uiEvent : Flow<UiEvent?> = _uiEvent.receiveAsFlow()

    private val _reminders = MutableStateFlow<List<Reminder>?>(null)
    val reminder : StateFlow<List<Reminder>?> = _reminders.asStateFlow()


    init {
        viewModelScope.launch(dispatcher){
          getAllReminders()
        }
    }




    fun onEvent(event : MainEvent) {

        when(event) {

            is MainEvent.SignOut -> {
                viewModelScope.launch(dispatcher) {
                    repository.signOut()
                    _uiEvent.send(UiEvent.Navigate(MainFragmentDirections.actionMainFragmentToLoginFragment2()))
                }

            }

            is MainEvent.DeleteReminder -> {
                viewModelScope.launch(dispatcher){
                    deleteReminder(event.reminder)
                }
            }
        }
    }


     suspend fun getAllReminders() {
        repository.getAllReminders().collect {
            _reminders.emit(it)
        }
    }

    private suspend fun deleteReminder(reminder: Reminder) {
       useCase.DeleteReminder(reminder).collect { response ->

            when(response) {
                is Response.Success -> {
                    _uiEvent.send(UiEvent.ShowToast(response.data))

                }

                is Response.Error -> {
                    _uiEvent.send(UiEvent.ShowToast(response.message))
                }
            }
        }


    }



}
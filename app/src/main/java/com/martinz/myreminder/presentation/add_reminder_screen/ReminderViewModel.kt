package com.martinz.myreminder.presentation.add_reminder_screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.PointOfInterest
import com.martinz.myreminder.core.myLog
import com.martinz.myreminder.core.util.Response
import com.martinz.myreminder.core.util.UiEvent
import com.martinz.myreminder.domain.model.Reminder
import com.martinz.myreminder.domain.repositoy.ReminderRepository
import com.martinz.myreminder.domain.use_cases.reminder.ReminderUseCase
import com.martinz.myreminder.presentation.register_screen.RegistrationFormEvent
import com.martinz.myreminder.presentation.register_screen.RegistrationFormState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named


@HiltViewModel
class ReminderViewModel @Inject constructor(
    private val useCase: ReminderUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {



    private val _state = MutableStateFlow(ReminderState())
    var state : StateFlow<ReminderState> = _state.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent : Flow<UiEvent> = _uiEvent.consumeAsFlow()


    fun onEvent(event: ReminderEvent) {

        when (event) {

            is ReminderEvent.TitleChanged -> {
                _state.value = state.value.copy(
                    title = event.title
                )

            }
            is ReminderEvent.DescriptionChanged -> {
                _state.value = state.value.copy(
                    description = event.description
                )

            }
            is ReminderEvent.SliderChanged -> {
                _state.value = state.value.copy(
                    range = event.range
                )
            }

            is ReminderEvent.LocationChanged -> {
                _state.value = state.value.copy(
                    location = event.latLong
                )
            }

            is ReminderEvent.SaveReminder -> {
                viewModelScope.launch(dispatcher) {

                    when(true) {

                        state.value.title.isBlank() -> {
                            _uiEvent.send(UiEvent.ShowToast("Title can't be empty"))
                        }


                        state.value.location == null -> {
                            _uiEvent.send(UiEvent.ShowToast("No Location Selected"))
                        }

                        else -> {
                            val reminder = Reminder(title = state.value.title , description = state.value.description,
                                range = state.value.range,
                                latLong = state.value.location!!)
                            useCase.SaveReminder(reminder).collect { response ->
                                when (response) {
                                    is Response.Success -> {
                                        _uiEvent.send(UiEvent.Navigate(ReminderFragmentDirections.actionReminderFragmentToMainFragment()))
                                    }

                                    is Response.Error -> {
                                        _uiEvent.send(UiEvent.ShowToast(response.message))
                                    }
                                }

                            }
                        }
                    }


                }

            }

            is ReminderEvent.DeleteReminder -> {
                viewModelScope.launch(dispatcher) {
                    useCase.DeleteReminder(event.reminder).collect { response ->
                        when (response) {

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
        }

    }
}
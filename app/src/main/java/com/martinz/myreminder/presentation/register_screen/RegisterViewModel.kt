package com.martinz.myreminder.presentation.register_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.martinz.myreminder.core.myLog
import com.martinz.myreminder.core.util.Response
import com.martinz.myreminder.core.util.UiEvent
import com.martinz.myreminder.domain.repositoy.ReminderRepository
import com.martinz.myreminder.domain.use_cases.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val useCases: RegisterUseCase,
    private val repository: ReminderRepository
): ViewModel() {

    private val _state = MutableStateFlow(RegistrationFormState())
    val state : StateFlow<RegistrationFormState> = _state.asStateFlow()


    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()






    fun onEvent(event : RegistrationFormEvent) = when(event) {
        is RegistrationFormEvent.EmailChanged -> {
            _state.value = state.value.copy(
                email = event.email
            )

        }
        is RegistrationFormEvent.PasswordChanged -> {
            _state.value = state.value.copy(
                password = event.password
            )

        }
        is RegistrationFormEvent.RepeatedPasswordChanged -> {
            _state.value = state.value.copy(
                repeatedPassword = event.repeatedPassword
            )
        }
        is RegistrationFormEvent.Submit -> {
            myLog("Email : ${state.value.email} \n" +
            "Password : ${state.value.password} \n" +
            "Repeated Password : ${state.value.repeatedPassword}")
            submitData()

        }

        else -> {}

    }


    private fun submitData() {
        val emailResult = useCases.ValidateEmail(state.value.email)
        val passwordResult = useCases.ValidatePassword(state.value.password)
        val repeatedPasswordResult = useCases.ValidateRepeatedPassword(state.value.password , state.value.repeatedPassword)

        val hasError = listOf(
            emailResult,
            passwordResult,
            repeatedPasswordResult,
        ).any { !it.successful }

        if(hasError) {
            viewModelScope.launch {
                _uiEvent.send(UiEvent.ShowToast("Validation Error"))
                _state.emit(
                    RegistrationFormState(
                        emailError = emailResult.errorMessage,
                        passwordError = passwordResult.errorMessage,
                        repeatedPasswordError = repeatedPasswordResult.errorMessage,
                        email = state.value.email,
                        password = state.value.password,
                        repeatedPassword = state.value.repeatedPassword,
                    ))
            }
            return
        }
        viewModelScope.launch{
            repository.registerWithEmailAndPassword(state.value.email , state.value.password).collect { response ->
                when(response) {
                    is Response.Success -> {
                        _state.emit(
                            RegistrationFormState(
                                email = state.value.email,
                                password = state.value.password,
                                repeatedPassword = state.value.repeatedPassword,
                            )
                        )
                        _uiEvent.send(UiEvent.Navigate(RegisterFragmentDirections.actionRegisterFragmentToMainFragment()))
                    }

                    is Response.Error -> {
                        _uiEvent.send(UiEvent.ShowToast(response.message))
                    }
                }
            }

        }
    }



}
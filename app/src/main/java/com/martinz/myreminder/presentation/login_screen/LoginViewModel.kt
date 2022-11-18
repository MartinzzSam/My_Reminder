package com.martinz.myreminder.presentation.login_screen


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.martinz.myreminder.core.myLog
import com.martinz.myreminder.core.util.Response
import com.martinz.myreminder.core.util.UiEvent
import com.martinz.myreminder.domain.repositoy.ReminderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: ReminderRepository
) : ViewModel() {







    private val _state = MutableStateFlow<LoginState>(LoginState())
    val state = _state.asStateFlow()




    private val _uiEvent = Channel<UiEvent>()
    val uiEvent : Flow<UiEvent> = _uiEvent.consumeAsFlow()





    fun onEvent(event : LoginEvent) {
        when(event) {


            is LoginEvent.EmailChanged -> {
                _state.value = state.value.copy(
                    emailState = event.email
                )

            }
            is LoginEvent.PasswordChanged -> {
                _state.value = state.value.copy(
                    passwordState = event.password
                )

            }

            is LoginEvent.SignInWithGoogle -> {
                viewModelScope.launch(Dispatchers.IO) {
                    _state.emit(state.value.copy(isLoading = true))
                    repository.signWithGoogle(event.account).collect { response ->
                        when(response) {

                            is Response.Success -> {
                                _state.emit(state.value.copy(isLoading = false))
                                myLog(response.data)
                                _uiEvent.send(UiEvent.Navigate(LoginFragmentDirections.actionLoginFragmentToMainFragment()))
                            }

                            is Response.Error -> {
                                _state.emit(state.value.copy(isLoading = false))
                                myLog(response.message)
                            }

                        }
                    }
                }

            }

            is LoginEvent.SignInWithEmailAndPassword -> {
                viewModelScope.launch(Dispatchers.IO) {
                    _state.emit(state.value.copy(isLoading = true))
                    repository.signInWithEmailAndPassword(state.value.emailState , state.value.passwordState).collect { response ->
                        when(response) {
                         is Response.Success -> {
                             _state.emit(state.value.copy(isLoading = false))
                             _uiEvent.send(UiEvent.Navigate(LoginFragmentDirections.actionLoginFragmentToMainFragment()))
                         }

                         is Response.Error -> {
                             _state.emit(state.value.copy(isLoading = false))
                             _uiEvent.send(UiEvent.ShowToast(response.message))
                         }
                        }
                    }
                }

            }
        }

    }




}
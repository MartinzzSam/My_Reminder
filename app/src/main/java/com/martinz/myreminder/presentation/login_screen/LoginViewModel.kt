package com.martinz.myreminder.presentation.login_screen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.martinz.myreminder.core.myLog
import com.martinz.myreminder.core.util.Response
import com.martinz.myreminder.core.util.UiEvent
import com.martinz.myreminder.domain.repositoy.ReminderRepository
import com.martinz.myreminder.domain.use_cases.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: ReminderRepository
) : ViewModel() {






    val emailState = mutableStateOf("")

    val passwordState = mutableStateOf("")

    val isLoading = mutableStateOf(false)

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent : Flow<UiEvent> = _uiEvent.consumeAsFlow()





    fun onEvent(event : LoginEvent) {
        when(event) {

            is LoginEvent.SignInWithGoogle -> {
                isLoading.value = true
                viewModelScope.launch(Dispatchers.IO) {
                    repository.signWithGoogle(event.account).collect { response ->
                        when(response) {

                            is Response.Success -> {
                                isLoading.value = false
                                myLog(response.data)
                                _uiEvent.send(UiEvent.Navigate(LoginFragmentDirections.actionLoginFragmentToMainFragment()))
                            }

                            is Response.Error -> {
                                isLoading.value = true
                                myLog(response.message)
                            }

                        }
                    }
                }

            }

            is LoginEvent.SignInWithEmailAndPassword -> {
                isLoading.value = true
                viewModelScope.launch(Dispatchers.IO) {
                    repository.signInWithEmailAndPassword(emailState.value , passwordState.value).collect { response ->
                        when(response) {
                         is Response.Success -> {
                             isLoading.value = false
                             _uiEvent.send(UiEvent.Navigate(LoginFragmentDirections.actionLoginFragmentToMainFragment()))
                         }

                         is Response.Error -> {
                             isLoading.value = false
                             _uiEvent.send(UiEvent.ShowSnackBar(response.message))
                         }
                        }
                    }
                }

            }
        }

    }




}
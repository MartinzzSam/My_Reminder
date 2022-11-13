package com.martinz.myreminder.presentation.register_screen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.martinz.myreminder.R
import com.martinz.myreminder.core.uiEventObserver
import com.martinz.myreminder.databinding.FragmentMainBinding
import com.martinz.myreminder.databinding.FragmentRegisterBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest


@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val registerViewModel : RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launchWhenCreated {
            uiEventObserver(registerViewModel.uiEvent)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClicks()
        getValidationError()




    }



    private fun onClicks() {
        binding.apply {
            SignUpBtn.setOnClickListener { registerViewModel.onEvent(RegistrationFormEvent.Submit)}
            etEmail.addTextChangedListener(RegisterTextWatcher { registerViewModel.onEvent(RegistrationFormEvent.EmailChanged(it)) })
            etPassword.addTextChangedListener(RegisterTextWatcher { registerViewModel.onEvent(RegistrationFormEvent.PasswordChanged(it)) })
            etRepeatedPassword.addTextChangedListener(RegisterTextWatcher { registerViewModel.onEvent(RegistrationFormEvent.RepeatedPasswordChanged(it)) })
        }
    }

    private fun getValidationError() {
        lifecycleScope.launchWhenStarted {
            registerViewModel.state.collectLatest { data ->
                binding.apply {
                    etEmail.error = data.emailError
                    etPassword.error = data.passwordError
                    etRepeatedPassword.error = data.repeatedPasswordError
                }

            }
        }
    }



}
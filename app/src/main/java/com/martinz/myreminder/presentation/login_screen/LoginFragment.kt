package com.martinz.myreminder.presentation.login_screen

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.martinz.myreminder.R
import com.martinz.myreminder.core.uiEventObserver
import com.martinz.myreminder.databinding.FragmentLoginBinding
import com.martinz.myreminder.core.util.ReminderTextWatcher
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginFragment : Fragment() {
    //    private lateinit var composeView: ComposeView
    private lateinit var signInClient: GoogleSignInClient
    private lateinit var getContent: ActivityResultLauncher<Intent>
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!


    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)





        val signInRequest = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        signInClient = GoogleSignIn.getClient(requireActivity(), signInRequest)

        getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {

            val account = GoogleSignIn.getSignedInAccountFromIntent(it.data).result

            account.let {
                viewModel.onEvent(LoginEvent.SignInWithGoogle(account = account))
            }

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(layoutInflater)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClick()
        uiEventObserver(viewModel.uiEvent)
    }


    private fun onClick() {
        binding.apply {
            SignUpBtn.setOnClickListener { viewModel.onEvent(LoginEvent.SignInWithEmailAndPassword) }
            googleButton.setOnClickListener { getContent.launch(signInClient.signInIntent) }
            etEmail.addTextChangedListener(ReminderTextWatcher() { email -> viewModel.onEvent(LoginEvent.EmailChanged(email)) })
            etPassword.addTextChangedListener(ReminderTextWatcher() { password -> viewModel.onEvent(LoginEvent.PasswordChanged(password)) })
            tvRegister.setOnClickListener { findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment()) }
        }
    }


}




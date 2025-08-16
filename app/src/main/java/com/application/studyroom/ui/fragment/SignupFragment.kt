package com.application.studyroom.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.application.studyroom.R
import com.application.studyroom.data.model.UserCredential
import com.application.studyroom.databinding.FragmentSignupBinding
import com.application.studyroom.ui.activity.AuthActivity
import com.application.studyroom.ui.state.UiState
import com.application.studyroom.ui.validator.InputValidator
import com.application.studyroom.ui.viewmodel.AuthViewModel
import com.application.studyroom.utils.showSnakeBar
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignupFragment : Fragment() {

    private lateinit var binding: FragmentSignupBinding
    private val viewModel: AuthViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupInputValidators()
        attachClickListeners()
        subscribeObservers()
    }

    private fun attachClickListeners() {
        binding.btnSignup.setOnClickListener {
            validateAllFields()?.let {
                viewModel.signup(it)
            }
        }

        binding.btnLogin.setOnClickListener {
            findNavController().navigate(R.id.action_SignupFragment_to_LoginFragment)
        }
    }

    private fun setupInputValidators() {
        binding.edtEmail.onFocusChangeListener = View.OnFocusChangeListener { _, _ ->
            InputValidator.validateEmail(binding.tilEmail)
        }
        binding.edtPassword.onFocusChangeListener = View.OnFocusChangeListener { _, _ ->
            InputValidator.validatePassword(binding.tilPassword)
        }
        binding.edtCoPassword.onFocusChangeListener = View.OnFocusChangeListener { _, _ ->
            InputValidator.validateConformPassword(binding.tilPassword, binding.tilCoPassword)
        }
    }

    private fun validateAllFields(): UserCredential? {
        return if (InputValidator.validateEmail(binding.tilEmail)
            && InputValidator.validatePassword(binding.tilPassword)
            && InputValidator.validateConformPassword(binding.tilPassword, binding.tilCoPassword)
        ) {
            UserCredential(
                binding.edtEmail.text.toString(),
                binding.edtPassword.text.toString()
            )
        } else null
    }

    private fun subscribeObservers() {
        lifecycleScope.launch {
            viewModel.signupUiState.collectLatest {
                if (it !is UiState.Initial)
                    (requireActivity() as AuthActivity).setLoading(it is UiState.Loading)
                when (it) {
                    is UiState.Error -> {
                        enableAllFields(true)
                        showSnakeBar(binding.root, it.error)
                    }

                    UiState.Initial -> {
                        enableAllFields(true)
                    }

                    UiState.Loading -> {
                        enableAllFields(false)
                    }

                    is UiState.Success<FirebaseUser> -> {
                        enableAllFields(true)
                        (requireActivity() as AuthActivity).onAuthComplete()
                    }
                }
            }
        }
    }

    private fun enableAllFields(bool: Boolean) {
        binding.apply {
            btnLogin.isEnabled = bool
            btnGoogleAuth.isEnabled = bool
            tilEmail.isEnabled = bool
            tilPassword.isEnabled = bool
            tilCoPassword.isEnabled = bool
            tilCoPassword.isErrorEnabled = bool
        }
    }
}
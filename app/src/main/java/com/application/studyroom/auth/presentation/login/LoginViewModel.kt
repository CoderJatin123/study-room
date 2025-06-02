package com.application.studyroom.auth.presentation.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.studyroom.auth.domain.AuthRepository
import com.application.studyroom.auth.presentation.validation.EmailUtils
import com.application.studyroom.auth.presentation.validation.PasswordUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(): ViewModel() {
    private val _email = MutableStateFlow("")
    private var _isValidEmail = MutableStateFlow(false)
    private val _isValidPassword = MutableStateFlow(false)
    private val _password = MutableStateFlow("")
    private val _isValidEmailAndPassword = MutableStateFlow(false)
    val _isLoading = MutableStateFlow(false)

    var isValidEmail = _isValidEmail.asStateFlow()
    var isValidPassword = _isValidPassword.asStateFlow()
    var isLoading = _isLoading.asStateFlow()

    val email = _email.asStateFlow()
    val password = _password.asStateFlow()

    val emailError = MutableStateFlow<String?>(null)
    val passwordError = MutableStateFlow<String?>(null)

    fun validateEmail() {
        val result = EmailUtils.isValidEmail(email.value)
        if(email.value.trim().isEmpty()){
            emailError.update { "Empty fields not allowed" }
            _isValidEmail.update { false }
        }else if (!result) {
            emailError.update {
                "Please enter valid email."
            }
            _isValidEmail.update { false }
        } else {
            emailError.update { null }
            _isValidEmail.update { true }
        }
    }

    fun validatePassword() {
        val result = PasswordUtils.isValidPassword(password.value)

        if (password.value.trim().isEmpty()) {
            passwordError.update { "Empty fields not allowed" }
            _isValidPassword.update { false }
        } else if (result == null) {
            passwordError.update { null }
            _isValidPassword.update { true }
        } else {
            passwordError.update { result }
            _isValidPassword.update { false }
        }
    }

    fun setEmail(string: String) {
        _email.update { string }
    }

    fun setPassword(string: String) {
        _password.update { string }
    }
    private fun clearAllFields(){
        _isLoading.update { false }
        _email.update { "" }
        _password.update { "" }
        emailError.update { null }
        passwordError.update { null }
    }

    private fun validateAllField() {
        validateEmail()
        validatePassword()
    }

    fun login(onResult:(Boolean)->Unit): Boolean {
        validateAllField()

        if (isValidEmail.value && isValidPassword.value) {
            _isLoading.update { true }
            viewModelScope.launch {
                val result = AuthRepository.loginUser(email.value, password.value)
                if(result.isSuccess){
                    _isLoading.update { false }
                    onResult(true)
                }else{
                    _isLoading.update { false }
                    passwordError.update { "Incorrect email or password" }
                    onResult(false)
                }
            }
        }
        return true
    }
}
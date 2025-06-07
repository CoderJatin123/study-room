package com.application.studyroom.auth.presentation.register

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.studyroom.auth.components.CurrentScreen
import com.application.studyroom.auth.domain.AuthRepository
import com.application.studyroom.auth.presentation.validation.EmailUtils
import com.application.studyroom.auth.presentation.validation.PasswordUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.concurrent.Executor

class SignupViewModel: ViewModel() {

    private val _email = MutableStateFlow("")
    private var _isValidEmail = MutableStateFlow(false)
    private val _isValidPassword = MutableStateFlow(false)
    private val _isValidCoPassword = MutableStateFlow(false)
    private val _password = MutableStateFlow("")
    private val _confirmPassword = MutableStateFlow("")
    private val _terms = MutableStateFlow(false)
    private val _isValidEmailAndPassword = MutableStateFlow(false)
    val _isLoading = MutableStateFlow(false)

    var isValidEmail = _isValidEmail.asStateFlow()
    var isValidPassword = _isValidPassword.asStateFlow()
    var isValidCoPassword = _isValidCoPassword.asStateFlow()
    var isLoading = _isLoading.asStateFlow()

    val email = _email.asStateFlow()
    val password = _password.asStateFlow()
    val confirmPassword = _confirmPassword.asStateFlow()
    val terms = _terms.asStateFlow()

    val emailError = MutableStateFlow<String?>(null)
    val passwordError = MutableStateFlow<String?>(null)
    val confirmPasswordError = MutableStateFlow<String?>(null)

    init {
        clearAllFields()
    }

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
        setRegister()
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
        setRegister()
    }

    fun validateConfirmPassword() {
        if (confirmPassword.value.trim().isEmpty()) {
            confirmPasswordError.update { "Empty fields not allowed" }
            _isValidCoPassword.update { false }
        } else if (confirmPassword.value == password.value) {
            _isValidCoPassword.update { true }
            confirmPasswordError.update { null }
        } else {
            confirmPasswordError.update { "Password doesn't matched" }
            _isValidCoPassword.update { false }
        }
        setRegister()
    }

    fun validateAllField() {
        validateEmail()
        validatePassword()
        validateConfirmPassword()
    }

    fun signup(onResult:(Boolean)->Unit){
        validateEmail()
        validatePassword()
        validateConfirmPassword()

        if(isValidEmail.value && isValidPassword.value && isValidCoPassword.value && _terms.value){
            _isLoading.update { true }

            viewModelScope.launch {
                val result = AuthRepository.createUser(email.value, password.value)
                if(result.result){
                _isLoading.update { false }
                   onResult(true)
                }else{
                _isLoading.update { false }
                  onResult(false)
                        emailError.update { result.errorMessage }
                        _isValidEmail.update { false }
                }
            }
        }
    }


    private fun setRegister() {
        _isValidEmailAndPassword.update { isValidEmail.value && isValidPassword.value && isValidCoPassword.value }
    }

    fun setEmail(string: String) {
        _email.update { string }
    }

    fun setPassword(string: String) {
        _password.update { string }
    }

    fun setConfirmPassword(string: String) {
        _confirmPassword.update { string }
    }

    fun setTerms(boolean: Boolean) {
        _terms.update { boolean }
    }

    private fun clearAllFields(){
        _isLoading.update { false }
        _email.update { "" }
        _password.update { "" }
        _confirmPassword.update { "" }
        _terms.update { false }
        emailError.update { null }
        passwordError.update { null }
        confirmPasswordError.update { null }
    }
}
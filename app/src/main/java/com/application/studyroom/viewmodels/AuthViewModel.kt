package com.application.studyroom.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.studyroom.auth_ui.CurrentScreen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel: ViewModel() {

    private val _currentScreen = MutableStateFlow(CurrentScreen.BOARDING_SCREEN)

    private val _email = MutableStateFlow<String>("")
    private val _password = MutableStateFlow<String>("")
    private val _confirmPassword = MutableStateFlow<String>("")
    private val _terms = MutableStateFlow(false)

    val email = _email.asStateFlow()
    val password = _password.asStateFlow()
    val confirmPassword = _confirmPassword.asStateFlow()
    val terms = _terms.asStateFlow()

    val emailError = MutableStateFlow<String>("")
    val passwordError = MutableStateFlow<String>("")
    val confirmPasswordError = MutableStateFlow<String>("")

    init {
       viewModelScope.launch {
           _currentScreen.collectLatest {
               _email.update { "" }
               _password.update { "" }
               _confirmPassword.update { "" }
               _terms.update { false }
           }
       }
    }

    fun setCurrentScreen(screen: CurrentScreen){
        _currentScreen.update { screen }
    }

    fun setEmail(string: String){
        _email.update { string }
    }
    fun setPassword(string: String){
        _password.update { string }
    }
    fun setConfirmPassword(string: String){
        _confirmPassword.update { string }
    }
    fun setTerms(boolean: Boolean){
        _terms.update { boolean }
    }
}
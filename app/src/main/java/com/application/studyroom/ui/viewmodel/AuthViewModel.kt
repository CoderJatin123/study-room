package com.application.studyroom.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.studyroom.data.model.AuthResult
import com.application.studyroom.data.model.UserCredential
import com.application.studyroom.domain.repository.AuthRepository
import com.application.studyroom.ui.state.UiState
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {
    private val _loginUiState = MutableStateFlow<UiState<FirebaseUser>>(UiState.Initial)
    val loginUiState: StateFlow<UiState<FirebaseUser>> = _loginUiState.asStateFlow()

    private val _signupUiState = MutableStateFlow<UiState<FirebaseUser>>(UiState.Initial)
    val signupUiState: StateFlow<UiState<FirebaseUser>> = _signupUiState.asStateFlow()

    fun login(credential: UserCredential) {
        viewModelScope.launch {
            _loginUiState.emit(UiState.Loading)
            when(val result = authRepository.login(credential)){
                is AuthResult.Failed -> {
                    _loginUiState.emit(UiState.Error(result.error))
                }
                is AuthResult.Success->{
                    _loginUiState.emit(UiState.Success(result.user))
                }
            }
        }
    }
    fun signup(credential: UserCredential) {
        viewModelScope.launch {
            _loginUiState.emit(UiState.Loading)
            when(val result = authRepository.signup(credential)){
                is AuthResult.Failed -> {
                    _loginUiState.emit(UiState.Error(result.error))
                }
                is AuthResult.Success->{
                    _loginUiState.emit(UiState.Success(result.user))
                }
            }
        }
    }
}
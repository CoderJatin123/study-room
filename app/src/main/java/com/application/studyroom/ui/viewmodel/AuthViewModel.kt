package com.application.studyroom.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.application.studyroom.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {

}
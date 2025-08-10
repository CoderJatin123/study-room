package com.application.studyroom.data.model

import com.google.firebase.auth.FirebaseUser

sealed class AuthResult{
    data class Success(val user : FirebaseUser) : AuthResult()
    data class Failed(val error: String) : AuthResult()
}
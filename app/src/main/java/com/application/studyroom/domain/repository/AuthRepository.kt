package com.application.studyroom.domain.repository

import com.application.studyroom.data.model.AuthResult
import com.application.studyroom.data.model.UserCredential
import com.google.firebase.auth.FirebaseUser

interface AuthRepository {
    suspend fun login(credential: UserCredential) : AuthResult
    suspend fun signup(credential: UserCredential) : AuthResult
    fun signInWithGoogle()
    suspend fun isUserAvailable() : FirebaseUser?
    fun logout()
}
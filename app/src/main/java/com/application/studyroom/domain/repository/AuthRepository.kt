package com.application.studyroom.domain.repository

import com.application.studyroom.data.model.AuthResult
import com.application.studyroom.data.model.UserCredential

interface AuthRepository {
    suspend fun login(credential: UserCredential) : AuthResult
    suspend fun signup(credential: UserCredential) : AuthResult
}
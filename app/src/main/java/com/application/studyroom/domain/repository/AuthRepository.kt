package com.application.studyroom.domain.repository

import android.content.Context
import com.application.studyroom.data.model.AuthResult
import com.application.studyroom.data.model.UserCredential
import com.application.studyroom.domain.googe_auth.GoogleAuth
import com.google.firebase.auth.FirebaseUser

interface AuthRepository {
    suspend fun login(credential: UserCredential): AuthResult
    suspend fun signup(credential: UserCredential): AuthResult
    fun getGoogleAuthClient(context: Context): GoogleAuth
    fun isUserAvailable(): FirebaseUser?
    suspend fun logout()
}
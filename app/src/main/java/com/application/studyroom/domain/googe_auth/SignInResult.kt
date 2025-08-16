package com.application.studyroom.domain.googe_auth

import com.application.studyroom.data.model.UserData
import com.google.firebase.auth.FirebaseUser

data class SignInResult(
    val data: FirebaseUser?,
    val errorMessage: String?
)
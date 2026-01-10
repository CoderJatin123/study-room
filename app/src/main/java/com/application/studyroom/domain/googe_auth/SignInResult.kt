package com.application.studyroom.domain.googe_auth

import com.google.firebase.auth.FirebaseUser

data class SignInResult(
    val data: FirebaseUser?,
    val errorMessage: String?
)
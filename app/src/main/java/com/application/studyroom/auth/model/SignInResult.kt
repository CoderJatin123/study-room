package com.application.studyroom.auth.model

data class SignInResult(
    val data: UserData?,
    val errorMessage: String?
)


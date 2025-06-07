package com.application.studyroom.auth.model

data class SignInResult(
    val data: UserData?,
    val errorMessage: String?
)

class FResult(val result: Boolean, val errorMessage: String)


package com.application.studyroom.data.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class UserData(
    var userId: String = "",
    var name: String? = null,
    var email: String? = null,
    var profileUrl: String? = null
)
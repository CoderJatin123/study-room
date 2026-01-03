package com.application.studyroom.data.model

data class Announcement(
    var authorId: String? = null,
    var subject: String? = null,
    var description: String? = null,
    var timeStamp: Long? = null
)
package com.application.studyroom.room.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Room(
    var createdBy: String? = null,
    var createdByName: String? = null,
    val createdAt: Long? = null,
    val name: String?= null,
    val description: String?= null,
    val announcements: List<Announcement>? = null) {

    // Null default values create a no-argument default constructor, which is needed
    // for deserialization from a DataSnapshot.
}
package com.application.studyroom.data.model

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

class RoomItem(val isLoading: Boolean = false, val room: Room?= null)
fun Room.toRoomItem() = RoomItem(false, this)
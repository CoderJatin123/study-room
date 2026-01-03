package com.application.studyroom.data.model

import com.google.firebase.database.IgnoreExtraProperties
import com.google.gson.Gson

@IgnoreExtraProperties
data class Room(
    var code: String? = null,
    var createdBy: String? = null,
    var createdByName: String? = null,
    val createdAt: Long? = null,
    val name: String?= null,
    val description: String?= null,
    val announcements: List<Announcement>? = null) {

    // Null default values create a no-argument default constructor, which is needed
    // for deserialization from a DataSnapshot.
}

fun Room.toJson(): String {
    return Gson().toJson(this).toString()
}

fun String.getRoom(): Room? {
    return Gson().fromJson(this, Room::class.java)
}

class RoomItem(val isLoading: Boolean = false, val room: Room?= null)
fun Room.toRoomItem() = RoomItem(false, this)
package com.application.studyroom.domain.repository

import com.application.studyroom.data.model.Announcement
import com.application.studyroom.data.model.Room
import com.application.studyroom.domain.AnnouncementEntity
import com.application.studyroom.ui.state.UiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.random.Random

interface RoomRepository {
    suspend fun createRoom(
        name: String,
        description: String,
        state: MutableSharedFlow<UiState<String>>
    )

    suspend fun getRooms(roomsState: MutableStateFlow<UiState<List<Room>>>)
    suspend fun joinRoom(roomCode: String, state: MutableSharedFlow<UiState<Room>>)
    suspend fun createAnnouncement(
        roomId: String,
        announcement: Announcement,
        state: MutableStateFlow<UiState<Announcement>>
    )

    suspend fun getAllAnnouncementByRoomId(
        roomId: String,
        state: MutableSharedFlow<UiState<List<AnnouncementEntity>>>
    )

    fun getNewRoomCode(): String {
        val builder = StringBuilder()
        repeat(6) { index ->
            if (index % 2 == 0) {
                // Even index (0, 2, 4) → Alphabet
                val char = ('A'..'Z').random()
                builder.append(char)
            } else {
                // Odd index (1, 3, 5) → Digit
                val digit = Random.nextInt(0, 10)
                builder.append(digit)
            }
        }
        return builder.toString()
    }
}
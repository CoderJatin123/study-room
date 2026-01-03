package com.application.studyroom.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.studyroom.data.model.Announcement
import com.application.studyroom.data.model.Room
import com.application.studyroom.domain.repository.RoomRepository
import com.application.studyroom.ui.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnnouncementsViewModel @Inject constructor(val roomRepository: RoomRepository) : ViewModel() {
    lateinit var room: Room
    var announcement = Announcement()
    private val _announcementsListState =
        MutableStateFlow<UiState<List<Announcement>>>(UiState.Initial)
    val announcementsListState: StateFlow<UiState<List<Announcement>>> =
        _announcementsListState.asStateFlow()

    private val _createAnnouncementState =
        MutableStateFlow<UiState<Announcement>>(UiState.Initial)
    val createAnnouncementsListState: StateFlow<UiState<Announcement>> =
        _createAnnouncementState.asStateFlow()

    fun getAnnouncements(room: Room) {
        this.room = room
        room.code?.let {
            viewModelScope.launch {
                roomRepository.getAllAnnouncementByRoomId(it, _announcementsListState)
            }
        }
    }

    fun createAnnouncement(roomId: String, subject: String, description: String) {
        viewModelScope.launch {
            val announcement = Announcement(
                subject = subject,
                description = description
            )
            roomRepository.createAnnouncement(
                roomId,
                announcement,
                _createAnnouncementState
            )
        }
    }
}
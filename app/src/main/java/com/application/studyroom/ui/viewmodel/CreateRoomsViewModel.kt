package com.application.studyroom.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
class CreateRoomsViewModel @Inject constructor(private val roomRepository: RoomRepository) :
    ViewModel() {

    private val _createRoomState = MutableStateFlow<UiState<String>>(UiState.Initial)
    val creteRoomState: StateFlow<UiState<String>> = _createRoomState.asStateFlow()

    fun createRoom(name: String, description: String) {
        viewModelScope.launch {
            roomRepository.createRoom(name, description, _createRoomState)
        }
    }
}
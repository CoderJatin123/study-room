package com.application.studyroom.ui.viewmodel

import android.util.Log
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
class HomeViewModel @Inject constructor(val roomRepository: RoomRepository) : ViewModel() {

     private val _roomsState = MutableStateFlow<UiState<List<Room>>>(UiState.Initial)
    val roomsState: StateFlow<UiState<List<Room>>> = _roomsState.asStateFlow()

    fun refresh() {
        Log.d("Rooms", "HomeVM refresh: rooms")
        viewModelScope.launch {
            roomRepository.getRooms(_roomsState)
        }
    }
}
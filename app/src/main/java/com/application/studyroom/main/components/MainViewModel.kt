package com.application.studyroom.main.components

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.studyroom.main.presentation.components.CurrentScreen
import com.application.studyroom.room.domain.RoomRepository
import com.application.studyroom.room.models.Room
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(): ViewModel() {
     var _state = mutableStateOf(CurrentScreen.HOME)
        private set

     private val _joinedRooms = MutableStateFlow(emptyList<Room>())
      var joinedRooms = _joinedRooms.asStateFlow()


    fun setCurrentState(state: CurrentScreen){
        _state.value=state
    }

    init {
        viewModelScope.launch {
           RoomRepository.getListOfJoinedRooms().onSuccess {
                _joinedRooms.value = it
            }.onFailure {


            }
        }
    }
}
package com.application.studyroom.room.join.components

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.studyroom.room.domain.RoomRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class JoinRoomViewModel: ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _roomCode = MutableStateFlow<String?>(null)
    val roomCode: StateFlow<String?> = _roomCode

    private val _roomCodeError = MutableStateFlow<String?>(null)
    val roomCodeError: StateFlow<String?> = _roomCodeError

    fun setCode(code : String){
        _roomCode.value =  code
    }

    fun validateRoomCode(){

    }

    fun joinRoom(onSuccess:(String)->Unit){
        viewModelScope.launch {
            _roomCode.value?.let {
                _isLoading.update { true }
                val result = RoomRepository.addParticipantToRoom(it)
                if(result.isFailure){
                    _roomCodeError.update {result.exceptionOrNull()?.localizedMessage}
                }else{
                    onSuccess("You successfully joined the group")
                }
                _isLoading.update { false }
            }
        }
    }
}
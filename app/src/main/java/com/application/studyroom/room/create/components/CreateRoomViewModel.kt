package com.application.studyroom.room.create.components

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.studyroom.room.create.domain.utils.getNewRoomCode
import com.application.studyroom.room.domain.RoomRepository
import com.application.studyroom.room.models.Room
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CreateRoomViewModel : ViewModel() {

    private val _roomTitle = MutableStateFlow("")
    val roomTitle: StateFlow<String> = _roomTitle

    private val _roomDesc = MutableStateFlow("")
    val roomDesc: StateFlow<String> = _roomDesc

    private val _roomTitleError = MutableStateFlow<String?>(null)
    val roomTitleError: StateFlow<String?> = _roomTitleError

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _generatedRoomCode = MutableStateFlow<String?>(null)
    val generatedRoomCode: StateFlow<String?> = _generatedRoomCode

    fun setRoomTitle(value: String) {
        _roomTitle.value = value
    }

    fun setRoomDesc(value: String) {
        _roomDesc.value = value
    }

    fun validateTitle() {
        _roomTitleError.value = if (_roomTitle.value.length < 3) "Title too short" else null
    }

    fun createRoom(onComplete: (Boolean) -> Unit) {
        validateTitle()
        if (_roomTitleError.value != null) return

        val roomTitle = _roomTitle.value
        if (roomTitle.isNullOrBlank()) {
            _roomTitleError.value = "Room title cannot be empty"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true

            try {
                val code = getNewRoomCode()
                val result = RoomRepository.createRoom(
                    Room(
                        createdBy = null,
                        createdAt = System.currentTimeMillis(),
                        name = roomTitle,
                        description = _roomDesc.value ?: ""
                    ),
                    code = code
                )

                if (result.isSuccess) {
                    _generatedRoomCode.value = code
                    onComplete(true)
                } else {
                    Log.e("CreateRoom", "Failed: ${result.exceptionOrNull()?.message}")
                    onComplete(false)
                }
            } catch (e: Exception) {
                Log.e("CreateRoom", "Exception: ${e.message}")
                onComplete(false)
            } finally {
                _isLoading.value = false
            }
        }
    }
}

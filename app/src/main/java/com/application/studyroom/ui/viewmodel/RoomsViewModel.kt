package com.application.studyroom.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.application.studyroom.domain.repository.RoomRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RoomsViewModel @Inject constructor(private val roomRepository: RoomRepository) : ViewModel() {

}
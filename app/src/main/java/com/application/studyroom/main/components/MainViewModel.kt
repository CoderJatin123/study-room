package com.application.studyroom.main.components

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.application.studyroom.main.presentation.components.CurrentScreen

class MainViewModel(): ViewModel() {
     var _state = mutableStateOf(CurrentScreen.HOME)
        private set

    fun setCurrentState(state: CurrentScreen){
        _state.value=state
    }
}
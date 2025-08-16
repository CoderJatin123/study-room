package com.application.studyroom.ui.state

sealed class UiState<out T> {
    data object Loading: UiState<Nothing>()
    data object Initial: UiState<Nothing>()
    data class Success<T>(val data: T): UiState<T>()
    data class Error(val error: String) : UiState<Nothing>()
}
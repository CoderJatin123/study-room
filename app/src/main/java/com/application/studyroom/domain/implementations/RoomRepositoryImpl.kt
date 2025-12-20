package com.application.studyroom.domain.implementations

import android.util.Log
import com.application.studyroom.data.model.Room
import com.application.studyroom.domain.repository.RoomRepository
import com.application.studyroom.network.NetworkHelper
import com.application.studyroom.ui.state.UiState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RoomRepositoryImpl @Inject constructor(val networkHelper: NetworkHelper) : RoomRepository {

    private val database: DatabaseReference = Firebase.database.reference
    val auth: FirebaseAuth = Firebase.auth

    override suspend fun createRoom(room: Room): Flow<UiState<Room>> {
        val state = MutableSharedFlow<UiState<Room>>()
        state.emit(UiState.Loading)
        val code = getNewRoomCode()
        try {
            auth.currentUser?.let {
                room.createdBy = it.uid
                room.createdByName = auth.currentUser!!.displayName
                database.child("rooms").child(code).setValue(room).await()
                state.emit(UiState.Success(room))
            }

        } catch (e: Exception) {
            Log.e("RoomRepository", "Failed to create room", e)
            state.emit(UiState.Error("Failed to create room"))
        }
        return state
    }

    override suspend fun getRooms(): Flow<UiState<List<Room>>> {
        val state = MutableSharedFlow<UiState<List<Room>>>()
        state.emit(UiState.Loading)
        try {
            auth.currentUser?.let {
                val usersRef = database.child("users").child(it.uid).child("joined_rooms")
                val snapshot = usersRef.get().await()

                val joinedRooms = if (snapshot.exists()) {
                    snapshot.getValue(object : GenericTypeIndicator<List<String>>() {})
                        ?: emptyList()
                } else {
                    emptyList()
                }

                val rooms = mutableListOf<Room>()
                for (roomCode in joinedRooms) {
                    val roomSnapshot = database.child("rooms").child(roomCode).get().await()
                    if (roomSnapshot.exists()) {
                        roomSnapshot.getValue(object : GenericTypeIndicator<Room>() {})
                            ?.let { room ->
                                rooms.add(room)
                            }
                    };
                }
                state.emit(UiState.Success(rooms))

            }
        } catch (e: Exception) {
            Log.e("RoomRepository", "Failed to get joined rooms", e)
            state.emit(UiState.Error("Failed to get joined rooms"))
        }
        return state
    }

    override suspend fun joinRoom(): Flow<UiState<Room>> {
        val state = MutableSharedFlow<UiState<Room>>()
        return state
    }
}
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

    override suspend fun joinRoom(roomCode: String, state: MutableSharedFlow<UiState<Room>>) {
        state.emit(UiState.Loading)
        try {
            // Check if user is authenticated
            val currentUser = auth.currentUser
            if (currentUser == null) {
                state.emit(UiState.Error("User not authenticated"))
                return
            }

            val userId = currentUser.uid

            // Check if the room exists
            val roomRef = database.child("rooms").child(roomCode)
            val roomSnapshot = roomRef.get().await()

            if (!roomSnapshot.exists()) {
                Log.d("RoomRepository", "joinRoom: Room not found")
                state.emit(UiState.Error("Room not found"))
                return
            }

            // Get the room data
            val room = roomSnapshot.getValue(object : GenericTypeIndicator<Room>() {})
            if (room == null) {
                state.emit(UiState.Error("Failed to parse room data"))
                return
            }

            // Check if user is already a participant
            val participantsRef = roomRef.child("participants")
            val participantsSnapshot = participantsRef.get().await()

            val currentParticipants = if (participantsSnapshot.exists()) {
                participantsSnapshot.getValue(object : GenericTypeIndicator<List<String>>() {})
                    ?: emptyList()
            } else {
                emptyList()
            }

            if (currentParticipants.contains(userId)) {
                // User is already a participant, just return the room
                state.emit(UiState.Error("You have already joined this room"))
                return
            }

            // Add user to room's participants list
            val updatedParticipants = currentParticipants.toMutableList().apply {
                add(userId)
            }
            participantsRef.setValue(updatedParticipants).await()

            // Add room code to user's joined_rooms list
            val userJoinedRoomsRef = database.child("users").child(userId).child("joined_rooms")
            val userRoomsSnapshot = userJoinedRoomsRef.get().await()

            val currentJoinedRooms = if (userRoomsSnapshot.exists()) {
                userRoomsSnapshot.getValue(object : GenericTypeIndicator<List<String>>() {})
                    ?: emptyList()
            } else {
                emptyList()
            }

            // Only add if not already in the list (double-check for consistency)
            if (!currentJoinedRooms.contains(roomCode)) {
                val updatedJoinedRooms = currentJoinedRooms.toMutableList().apply {
                    add(roomCode)
                }
                userJoinedRoomsRef.setValue(updatedJoinedRooms).await()
            }

            state.emit(UiState.Success(room))

        } catch (e: Exception) {
            Log.e("RoomRepository", "Failed to join room: $roomCode", e)
            state.emit(UiState.Error("Failed to join room: ${e.localizedMessage ?: "Unknown error"}"))
        }
        return
    }
}
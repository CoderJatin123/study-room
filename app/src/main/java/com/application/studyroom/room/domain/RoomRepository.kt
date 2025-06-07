package com.application.studyroom.room.domain

import android.util.Log
import com.application.studyroom.auth.model.FResult
import com.application.studyroom.room.models.Room
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

object RoomRepository {
    private val database: DatabaseReference = Firebase.database.reference
    private val auth: FirebaseAuth = Firebase.auth

    suspend fun createRoom(room: Room, code: String): Result<String> {
        return try {
            val userId = auth.currentUser?.uid
                ?: return Result.failure(Exception("User not authenticated"))

            room.createdBy = userId
            room.createdByName= auth.currentUser!!.displayName

            database.child("rooms").child(code).setValue(room).await()
            Result.success(code)
        } catch (e: Exception) {
            Log.e("RoomRepository", "Failed to create room", e)
            Result.failure(e)
        }
    }

    suspend fun addParticipantToRoom(roomCode: String): Result<String> {
        return try {

            val participantId = auth.currentUser?.uid
                ?: return Result.failure(Exception("Unauthorized user."))

            val room= database.child("rooms").child(roomCode).get().await()

            if(!room.exists()){
                Log.d("TAG", "addParticipantToRoom: Invalid")
                return Result.failure<String>(Exception("Invalid room code"))
            }

            val participantsRef = database.child("rooms").child(roomCode).child("participants")
            val snapshot = participantsRef.get().await()

            val currentParticipants = if (snapshot.exists()) {
                snapshot.getValue(object : GenericTypeIndicator<MutableList<String>>() {}) ?: mutableListOf()
            } else {
                mutableListOf()
            }

            if (!currentParticipants.contains(participantId)) {
                currentParticipants.add(participantId)
                participantsRef.setValue(currentParticipants).await()

                val usersRef = database.child("users").child(participantId).child("joined_rooms")
                val rooms = usersRef.get().await()

                val currentRooms = if (rooms.exists()) {
                    rooms.getValue(object : GenericTypeIndicator<MutableList<String>>() {}) ?: mutableListOf()
                } else {
                    mutableListOf()
                }

                if (!currentRooms.contains(roomCode)) {
                    currentRooms.add(roomCode)
                    usersRef.setValue(currentRooms).await()
                }
                return Result.success("Room joined")
            }else{
                return Result.failure(Exception("You are already joined this room"))
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getListOfJoinedRooms(): Result<List<Room>> {
        return try {
            val userId = auth.currentUser?.uid
                ?: return Result.failure(Exception("Unauthorized user."))

            val usersRef = database.child("users").child(userId).child("joined_rooms")
            val snapshot = usersRef.get().await()

            val joinedRooms = if (snapshot.exists()) {
                snapshot.getValue(object : GenericTypeIndicator<List<String>>() {}) ?: emptyList()
            } else {
                emptyList()
            }

            val rooms = mutableListOf<Room>()
            for (roomCode in joinedRooms) {
                val roomSnapshot = database.child("rooms").child(roomCode).get().await()
                if(roomSnapshot.exists()){
                    roomSnapshot.getValue(object : GenericTypeIndicator<Room>() {})?.let { room ->
                        rooms.add(room)
                    }
                };
            }
            Result.success(rooms)

        } catch (e: Exception) {
            Log.e("RoomRepository", "Failed to get joined rooms", e)
            Result.failure(e)
        }
    }
}
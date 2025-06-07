package com.application.studyroom.auth.domain

import android.content.Context
import com.application.studyroom.auth.model.FResult
import com.application.studyroom.auth.model.UserData
import com.application.studyroom.room.domain.RoomRepository
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.cancellation.CancellationException

object AuthRepository {
    private var auth: FirebaseAuth = Firebase.auth
    var currentUser: FirebaseUser? = auth.currentUser
    private val database: DatabaseReference = Firebase.database.reference

    suspend fun createUser(email: String, password: String): FResult {
        return try {
            val user =  auth.createUserWithEmailAndPassword(email, password).await()
            val userId= user.user?.uid  ?: ""

            val usersRef = database.child("users").child(userId)
            val snapshot = usersRef.get().await()

            val currentUsers = if (snapshot.exists()) {
                snapshot.getValue(object : GenericTypeIndicator<MutableList<String>>() {}) ?: mutableListOf()
            } else {
                mutableListOf()
            }

            if (!currentUsers.contains(userId)) {
                currentUsers.add(userId)
                usersRef.setValue(currentUsers).await()
                usersRef.child("display_name").setValue(user.user?.displayName.toString()).await()
                usersRef.child("email").setValue(user.user?.email.toString()).await()
                usersRef.child("profilePictureUrl").setValue(user.user?.photoUrl.toString()).await()
            }

            FResult(true,"")

        } catch (e: Exception) {
            val error = e.localizedMessage.toString()
            if(error.contains("email address is already in use")){
              FResult(false,error)
            } else {
                FResult(false,"Unknown error occurred")
            }
        }
    }


    suspend fun loginUser(email: String, password: String): FResult {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            FResult(true,"")
        } catch (e: Exception) {
                FResult(false,"Incorrect email or password")
        }
    }

    fun getSignedInUser(): UserData? = auth.currentUser?.run {
        UserData(
            userId = uid,
            username = displayName,
            email = email,
            profilePictureUrl = photoUrl?.toString()
        )
    }


    suspend fun signOut() {
        try {
            //oneTapClient: SignInClient
            //oneTapClient.signOut().await()
            auth.signOut()
        } catch(e: Exception) {
            e.printStackTrace()
            if(e is CancellationException) throw e
        }
    }
}
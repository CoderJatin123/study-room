package com.application.studyroom.domain.implementations

import com.application.studyroom.data.model.AuthResult
import com.application.studyroom.data.model.UserCredential
import com.application.studyroom.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class FirebaseAuthentication : AuthRepository {

    private var auth: FirebaseAuth = Firebase.auth
    override suspend fun login(credential: UserCredential): AuthResult {
        return try {
            val result = withContext(Dispatchers.IO) {
                auth.signInWithEmailAndPassword(credential.email, credential.password).await()
            }
            AuthResult.Success(result.user!!)
        } catch (e: Exception) {
            AuthResult.Failed(e.localizedMessage ?: "Something went wrong. Please try again later.")
        }
    }


    override suspend fun signup(credential: UserCredential): AuthResult {
        return try {
            val result = withContext(Dispatchers.IO) {
                auth.createUserWithEmailAndPassword(credential.email, credential.password).await()
            }
            AuthResult.Success(result.user!!)
        } catch (e: Exception) {
            AuthResult.Failed(e.localizedMessage ?: "Something went wrong. Please try again later.")
        }
    }
}
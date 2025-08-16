package com.application.studyroom.domain.implementations

import android.content.Context
import androidx.credentials.GetCredentialRequest
import com.application.studyroom.R
import com.application.studyroom.data.model.AuthResult
import com.application.studyroom.data.model.UserCredential
import com.application.studyroom.domain.repository.AuthRepository
import com.application.studyroom.utils.Constants
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
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


    override suspend fun isUserAvailable() = auth.currentUser
    override fun logout() = auth.signOut()

    override fun signInWithGoogle() {
        val googleIdOption = GetGoogleIdOption.Builder()
            .setServerClientId(Constants.WEB_CLIENT_ID)
            .setFilterByAuthorizedAccounts(true).build()

        val request = GetCredentialRequest.Builder().addCredentialOption(googleIdOption).build()
    }
}
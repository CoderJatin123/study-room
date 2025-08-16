package com.application.studyroom.domain.googe_auth

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import com.application.studyroom.R
import com.application.studyroom.data.model.UserData
import com.application.studyroom.utils.Constants
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.cancellation.CancellationException


class GoogleAuth(
    private val oneTapClient: SignInClient
) {
    private val auth = Firebase.auth
    private val database: DatabaseReference = Firebase.database.reference
    suspend fun signIn(): IntentSender? {
        val result = try {
            oneTapClient.beginSignIn(
                buildSignInRequest()
            ).await()
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            null
        }
        return result?.pendingIntent?.intentSender
    }

    suspend fun signInWithIntent(intent: Intent): SignInResult {
        val credential = oneTapClient.getSignInCredentialFromIntent(intent)
        val googleIdToken = credential.googleIdToken
        val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)
        return try {
            val user = auth.signInWithCredential(googleCredentials).await().user
            val userId = user?.uid ?: ""

            val usersRef = database.child("users").child(userId)
            val snapshot = usersRef.get().await()

            if (!snapshot.exists()) {
                usersRef.child("display_name").setValue(user?.displayName.toString()).await()
                usersRef.child("email").setValue(user?.email.toString()).await()
                usersRef.child("profilePictureUrl").setValue(user?.photoUrl.toString()).await()
            }

            SignInResult(
                data = user,
                errorMessage = null
            )
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            SignInResult(
                data = null,
                errorMessage = e.message
            )
        }
    }

    suspend fun signOut() {
        try {
            oneTapClient.signOut().await()
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
        }
    }

    private fun buildSignInRequest(): BeginSignInRequest {
        return BeginSignInRequest.Builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(Constants.WEB_CLIENT_ID)
                    .build()
            )
            .setAutoSelectEnabled(true)
            .build()
    }
}
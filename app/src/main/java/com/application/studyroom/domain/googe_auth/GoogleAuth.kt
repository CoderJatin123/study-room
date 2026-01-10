package com.application.studyroom.domain.googe_auth

import android.content.Context
import android.util.Log
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.ClearCredentialStateRequest.Companion.TYPE_CLEAR_CREDENTIAL_STATE
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.application.studyroom.utils.Constants.WEB_CLIENT_ID
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await


class GoogleAuth(
    private val context: Context
) {
    private val auth = Firebase.auth
    private val database: DatabaseReference = Firebase.database.reference
    val credentialManager = CredentialManager.create(context)

    val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
        .setFilterByAuthorizedAccounts(true)
        .setServerClientId(WEB_CLIENT_ID)
        .setAutoSelectEnabled(true)
        .build()

    val signInWithGoogleOption: GetSignInWithGoogleOption = GetSignInWithGoogleOption.Builder(
        serverClientId = WEB_CLIENT_ID
    ).build()

    val request: GetCredentialRequest = GetCredentialRequest.Builder()
        .addCredentialOption(signInWithGoogleOption)
        .build()

    suspend fun signInWithIntent(
        onSuccess: (FirebaseUser) -> Unit,
        onFailed: (String) -> Unit
    ) {
        try {
            val result = credentialManager.getCredential(
                request = request,
                context = context,
            ).credential

            if (result is CustomCredential) {
                if (result.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        val googleIdTokenCredential = GoogleIdTokenCredential
                            .createFrom(result.data)
                        val googleCredentials =
                            GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken, null)
                        try {
                            val user = auth.signInWithCredential(googleCredentials).await().user
                            user?.let { user ->
                                val userId = user.uid

                                val usersRef = database.child("users").child(userId)
                                val snapshot = usersRef.get().await()

                                if (!snapshot.exists()) {
                                    usersRef.child("name")
                                        .setValue(user.displayName.toString()).await()
                                    usersRef.child("email").setValue(user.email.toString()).await()
                                    usersRef.child("profileUrl")
                                        .setValue(user.photoUrl.toString()).await()
                                }
                                SignInResult(
                                    data = user,
                                    errorMessage = null
                                )
                                onSuccess(user)
                            } ?: run {
                                onFailed("Invalid user")
                            }
                        } catch (e: Exception) {
                            onFailed(e.localizedMessage?.toString() ?: "")
                        }
                    } catch (e: GoogleIdTokenParsingException) {
                        onFailed("Invalid google id")
                        Log.e("TAG", "Received an invalid google id token response", e)
                    }
                }
            }
        } catch (e: GetCredentialException) {
            onFailed(e.errorMessage.toString())
        }
    }

    suspend fun signOut() {
        credentialManager.clearCredentialState(
            ClearCredentialStateRequest(
                TYPE_CLEAR_CREDENTIAL_STATE
            )
        )
    }
}
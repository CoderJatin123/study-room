package com.application.studyroom.domain.implementations

import android.content.Context
import android.util.Log
import com.application.studyroom.data.model.AuthResult
import com.application.studyroom.data.model.UserCredential
import com.application.studyroom.domain.googe_auth.GoogleAuth
import com.application.studyroom.domain.repository.AuthRepository
import com.application.studyroom.network.NetworkHelper
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FirebaseAuthentication @Inject constructor(private val networkHelper: NetworkHelper) :
    AuthRepository {

    private var auth: FirebaseAuth = Firebase.auth
    private var googleAuth: GoogleAuth? = null

    override suspend fun login(credential: UserCredential): AuthResult {
        if (!networkHelper.isInternetAvailable())
            return AuthResult.Failed("No internet connection was found.")
        return try {
            withContext(Dispatchers.IO) {
                auth.signInWithEmailAndPassword(credential.email, credential.password).await()
            }.user?.let {
                AuthResult.Success(it)
            } ?: AuthResult.Failed("Unauthenticated user")
        } catch (e: Exception) {
            AuthResult.Failed(e.localizedMessage ?: "Something went wrong. Please try again later.")
        }
    }

    override suspend fun signup(credential: UserCredential): AuthResult {
        if (!networkHelper.isInternetAvailable())
            return AuthResult.Failed("No internet connection was found.")
        return try {
            withContext(Dispatchers.IO) {
                auth.createUserWithEmailAndPassword(credential.email, credential.password).await()
            }.user?.let {
                AuthResult.Success(it)
            } ?: AuthResult.Failed("Unauthenticated user")
        } catch (e: Exception) {
            AuthResult.Failed(e.localizedMessage ?: "Something went wrong. Please try again later.")
        }
    }

    override fun getGoogleAuthClient(context: Context): GoogleAuth {
        if (googleAuth == null) googleAuth = GoogleAuth(Identity.getSignInClient(context))
        return googleAuth!!
    }

    override fun isUserAvailable(): FirebaseUser? {
        Log.d("FirebaseAuth", "isUserAvailable: ${auth?.currentUser?.uid}")
        return auth.currentUser
    }

    override suspend fun logout() {
        googleAuth?.apply {
            signOut()
        }
        auth.signOut()
    }
}
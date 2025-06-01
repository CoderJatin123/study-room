package com.application.studyroom.auth.presentation.login

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.Credential
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.application.studyroom.R
import com.application.studyroom.auth.components.AuthViewModel
import com.application.studyroom.ui.StudyRoomTheme
import com.application.studyroom.ui.theme.ButtonText
import com.application.studyroom.ui.theme.MyButton
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.google.firebase.Firebase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth


@Composable
fun LoginScreen(modifier: Modifier = Modifier, navHostController: NavHostController,
                viewModel: AuthViewModel = AuthViewModel()
) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(all = 12.dp)
            .padding(bottom = 12.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp, alignment = Alignment.CenterVertically)
    ) {

        Image(
            modifier = Modifier
                .width(100.dp)
                .padding(top = 20.dp, bottom = 25.dp)
                .align(Alignment.CenterHorizontally),
            painter = painterResource(R.drawable.app_logo),
            contentDescription = ""
        )

        Text(modifier = Modifier.padding(start = 2.dp), text = "Continue with chapter", color = colorResource(
            R.color.green_dark)
        )
        Text(
            text = "Login to your Account",
            modifier = Modifier.padding(top = 2.dp, bottom = 22.dp, start = 2.dp),
            color = colorResource(R.color.title),
            fontSize = 32.sp, fontWeight = FontWeight.W700
        )

        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)) {
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp)),
                label = {
                    Text(text = "Email")
                },
                value = "", onValueChange = {
                    viewModel.setEmail(it)
                },
                colors = TextFieldDefaults.colors( focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent)
            )
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp)),
                label = {  Text(text = "Password") },
                value = "", onValueChange = {
                },
                colors = TextFieldDefaults.colors( focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent)
            )

        }

        MyButton(text = "Login", true) {

        }
        Text("OR",modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
        OutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(15),
            onClick ={

                val googleIdOption = GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(true)
                    .setServerClientId(context.getString(R.string.default_web_client_id))
                    .build()

                val request = GetCredentialRequest.Builder()
                    .addCredentialOption(googleIdOption)
                    .build()

                // I am stooped here
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = colorResource(R.color.gray),
            ), border = BorderStroke(1.dp, colorResource(R.color.gray))
        ) {
            ButtonText("Signing with ")
            Image(
                modifier = Modifier.padding(start = 2.dp),
                painter = painterResource(R.drawable.icon_google),
                contentDescription = "Google Icon"
            )
        }
        TextButton(onClick = {},modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.CenterHorizontally),) {
            Text("Don’t have an account, ")
            Text("Register here", color = colorResource(R.color.blue))
        }

    }
}

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    StudyRoomTheme {
        LoginScreen(navHostController = rememberNavController())
    }
}
fun handleSignIn(credential: Credential) {
    // Check if credential is of type Google ID
    if (credential is CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
        // Create Google ID Token
        val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)

        // Sign in to Firebase with using the token
        val credential = GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken, null)
        Firebase.auth.signInWithCredential(credential)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("TAG", "signInWithCredential:success: ")
                    val user = Firebase.auth.currentUser

                } else {
                    // If sign in fails, display a message to the user
                    Log.d("TAG", "signInWithCredential:failure", task.exception)
                }
            }
    } else {
        Log.w("TAG", "Credential is not of type Google ID!")
    }
}





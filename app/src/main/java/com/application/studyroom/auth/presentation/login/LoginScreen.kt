package com.application.studyroom.auth.presentation.login

import android.util.Log
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.Credential
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.application.studyroom.R
import com.application.studyroom.auth.components.AuthActivity
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
import kotlinx.coroutines.flow.update


@Composable
fun LoginScreen(modifier: Modifier = Modifier, navHostController: NavHostController,
                viewModel: LoginViewModel = LoginViewModel()) {

    val activity = (LocalActivity.current as AuthActivity)
    val email by viewModel.email.collectAsState()
    val emailError by viewModel.emailError.collectAsState()
    val password by viewModel.password.collectAsState()
    val passwordError by viewModel.passwordError.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    var isVisiblePassword by remember { mutableStateOf(false) }

    var isEmailFocused by remember { mutableStateOf(false) }
    var isPasswordFocused by remember { mutableStateOf(false) }


    Box(modifier = Modifier.fillMaxSize()) {

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

            Text(
                modifier = Modifier.padding(start = 2.dp),
                text = "Continue with old chapter",
                color = colorResource(R.color.green_dark)
            )
            Text(
                text = "Login to your an account",
                modifier = Modifier.padding(top = 2.dp, bottom = 22.dp, start = 2.dp),
                color = colorResource(R.color.title),
                fontSize = 32.sp, fontWeight = FontWeight.W700
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .onFocusChanged { focusState ->
                            if (isEmailFocused && !focusState.isFocused) {
                                viewModel.validateEmail()
                            }
                            isEmailFocused = focusState.isFocused
                        },
                    label = {
                        Text(text = "Email")
                    },
                    value = email,
                    onValueChange = {
                        viewModel.setEmail(it.trim())
                    },
                    supportingText = null,
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                )

                if (emailError != null) {
                    Text(
                        text = emailError.toString(),
                        color = Color.Red,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 4.dp),
                        fontSize = 14.sp
                    )

                }

                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .onFocusChanged { focusState ->
                            if (isPasswordFocused && !focusState.isFocused) {
                                viewModel.validatePassword()
                            }
                            isPasswordFocused = focusState.isFocused
                        },
                    label = { Text(text = "Password") },
                    value = password,
                    visualTransformation = if (isVisiblePassword) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },
                    onValueChange = { viewModel.setPassword(it.trim()) },
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    singleLine = true,
                    trailingIcon = {
                        IconButton(
                            modifier = Modifier
                                .width(30.dp)
                                .height(30.dp), onClick = {
                                isVisiblePassword = !isVisiblePassword
                            }) {
                            Icon(
                                modifier = Modifier.padding(4.dp),
                                painter = painterResource(if (isVisiblePassword) R.drawable.icon_open_eye else R.drawable.icon_eye_close),
                                contentDescription = null
                            )
                        }
                    }
                )

                if (passwordError != null) {
                    Text(
                        text = passwordError.toString(),
                        color = Color.Red,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 4.dp),
                        fontSize = 14.sp
                    )
                }


                MyButton(text = "Login", true, {
                    isPasswordFocused = true
                    isEmailFocused = true
                    isEmailFocused = true

                     viewModel.login {
                         if (it) {
                             activity.onAuthCompleted()
                         }
                     }
                })

                Text("OR", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
                OutlinedButton(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(15), onClick = {

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
                TextButton(
                    onClick = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally),
                ) {
                    Text("Don't have an account ? ")
                    Text("Register here", color = colorResource(R.color.blue))
                }

            }
        }
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(Color.Black.copy(alpha = 0.2f)) // semi-transparent background
                        .pointerInput(Unit) {},
                    contentAlignment = Alignment.Center
                ) {
                }

            }
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





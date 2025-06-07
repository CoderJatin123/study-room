package com.application.studyroom.auth.presentation.login

import android.app.Activity.RESULT_OK
import android.util.Log
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.application.studyroom.R
import com.application.studyroom.auth.components.AuthActivity
import com.application.studyroom.auth.domain.GoogleAuth
import com.application.studyroom.ui.StudyRoomTheme
import com.application.studyroom.ui.theme.ButtonText
import com.application.studyroom.ui.theme.MyButton
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.launch


@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = LoginViewModel(),
    activity: AuthActivity? = null
) {

    val googleAuthUiClient by lazy {
        GoogleAuth(
            context = activity!!.applicationContext,
            oneTapClient = Identity.getSignInClient(activity.applicationContext)
        )
    }

    val email by viewModel.email.collectAsState()
    val emailError by viewModel.emailError.collectAsState()
    val password by viewModel.password.collectAsState()
    val passwordError by viewModel.passwordError.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    var isVisiblePassword by remember { mutableStateOf(false) }

    var isEmailFocused by remember { mutableStateOf(false) }
    var isPasswordFocused by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = { result ->
            if (result.resultCode == RESULT_OK) {
                activity!!.lifecycleScope.launch {
                    val signInResult = googleAuthUiClient.signInWithIntent(
                        intent = result.data ?: return@launch
                    )
                    if (signInResult.data != null) {
                        activity.onAuthCompleted()
                    }
                }
            }
        }
    )

    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 18.dp, vertical = 16.dp)
                .padding(bottom = 22.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp, alignment = Alignment.CenterVertically)
        ) {

            Text(
                text = "Welcome back",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = colorResource(R.color.green_dark),
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 0.5.sp
                ),
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = "Sign In",
                style = MaterialTheme.typography.headlineLarge.copy(
                    color = colorResource(R.color.title),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 36.sp,
                    letterSpacing = (-0.5).sp
                )
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp).padding(top = 20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusChanged { focusState ->
                            if (isEmailFocused && !focusState.isFocused) {
                                viewModel.validateEmail()
                            }
                            isEmailFocused = focusState.isFocused
                        },
                    value = email,
                    onValueChange = { viewModel.setEmail(it.trim()) },
                    label = { Text("Email Address") },
                    placeholder = { Text("Enter your email") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    singleLine = true,
                    isError = emailError != null,
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = colorResource(R.color.green_dark),
                        errorBorderColor = MaterialTheme.colorScheme.error
                    )
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

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
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
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    onValueChange = { viewModel.setPassword(it.trim()) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = colorResource(R.color.green_dark),
                        errorBorderColor = MaterialTheme.colorScheme.error
                    ),
                    shape = RoundedCornerShape(8.dp),
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

                // Forgot Password Link
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    TextButton(
                        onClick = { /* Navigate to forgot password */ }
                    ) {
                        Text(
                            text = "Forgot Password?",
                            color = colorResource(R.color.green_dark),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Medium
                            )
                        )
                    }
                }
            }

            MyButton(text = "Sign In", true) {
                isEmailFocused = true
                isPasswordFocused = true

                viewModel.login { isSuccess ->
                    if (isSuccess) {
                        activity!!.onAuthCompleted()
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Divider(modifier = Modifier.weight(1f))
                Text(
                    text = "OR",
                    modifier = Modifier.padding(horizontal = 16.dp),
                    style = MaterialTheme.typography.bodySmall,
                    color = colorResource(R.color.gray)
                )
                Divider(modifier = Modifier.weight(1f))
            }

            OutlinedButton(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                onClick = {
                    activity!!.lifecycleScope.launch {
                        val signInIntentSender = googleAuthUiClient.signIn()
                        launcher.launch(
                            IntentSenderRequest.Builder(
                                signInIntentSender ?: return@launch
                            ).build()
                        )
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = colorResource(R.color.gray),
                ), border = BorderStroke(1.dp, colorResource(R.color.gray))
            ) {
                Image(
                    modifier = Modifier.padding(end = 8.dp),
                    painter = painterResource(R.drawable.icon_google),
                    contentDescription = "Google Icon"
                )
                ButtonText("Continue with Google ")
            }

            Box(modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center){
                TextButton(
                    onClick = { /* Navigate to signup */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    Text(
                        text = buildAnnotatedString {
                            append("Don't have an account? ")
                            withStyle(
                                style = SpanStyle(
                                    color = colorResource(R.color.green_dark),
                                    fontWeight = FontWeight.SemiBold
                                )
                            ) {
                                append("Sign Up")
                            }
                        },
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center),
                contentAlignment = Alignment.Center
            ){
                CircularProgressIndicator()
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(Color.Black.copy(alpha = 0.2f)) // semi-transparent background
                        .pointerInput(Unit) {} // absorb all input
                        .align(Alignment.Center),
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
        LoginScreen()
    }
}

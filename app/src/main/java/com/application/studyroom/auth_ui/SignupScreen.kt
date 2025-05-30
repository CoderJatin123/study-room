package com.application.studyroom.auth_ui

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.application.studyroom.R
import com.application.studyroom.ui.StudyRoomTheme
import com.application.studyroom.ui.theme.ButtonText
import com.application.studyroom.ui.theme.MyButton
import com.application.studyroom.viewmodels.AuthViewModel

@Composable
fun SignupScreen(modifier: Modifier = Modifier, navHostController: NavHostController,
                 viewModel: AuthViewModel = AuthViewModel()
) {

    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    val confirmPassword by viewModel.confirmPassword.collectAsState()
    val terms by viewModel.terms.collectAsState()

    var isVisiblePassword by remember { mutableStateOf(false) }
    var isVisibleCoPassword by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(all = 12.dp)
            .padding(bottom = 12.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp, alignment = Alignment.CenterVertically)
    ) {

        Image(
            modifier = Modifier.width(100.dp).padding(top = 20.dp, bottom = 25.dp).align(Alignment.CenterHorizontally),
            painter = painterResource(R.drawable.app_logo),
            contentDescription = ""
        )

         Text(modifier = Modifier.padding(start = 2.dp), text = "Start with new chapter", color = colorResource(R.color.green_dark))
        Text(
            text = "Create an account",
            modifier = Modifier.padding(top = 2.dp, bottom = 22.dp, start = 2.dp),
            color = colorResource(R.color.title),
            fontSize = 32.sp, fontWeight = FontWeight.W700
        )

        Column(modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)) {
            TextField(
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(6.dp)),
                label = {
                    Text(text = "Email")
                },
                supportingText = {
                    Text("Enter valid email", color = Color.Red)
                },
                value = email, onValueChange = { viewModel.setEmail(it)},
                colors = TextFieldDefaults.colors( focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent)
            )
            TextField(
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(6.dp)),
                label = {  Text(text = "Password") },
                value = password,
                visualTransformation = if(isVisiblePassword) { VisualTransformation.None }
                else { PasswordVisualTransformation() },
                onValueChange = { viewModel.setPassword(it) },
                colors = TextFieldDefaults.colors( focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent),
                singleLine = true,
                trailingIcon = {
                    IconButton(modifier = Modifier.width(30.dp).height(30.dp), onClick = {
                        isVisiblePassword=!isVisiblePassword
                    }){
                            Icon(
                                modifier = Modifier.padding(4.dp),
                                painter = painterResource( if(isVisiblePassword) R.drawable.icon_open_eye else R.drawable.icon_eye_close), contentDescription = null
                            )
                    }
                }
            )

            TextField(
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(6.dp)),
                label = {  Text(text = "Confirm password") },
                value = confirmPassword,
                visualTransformation = if(isVisibleCoPassword) { VisualTransformation.None }
                else { PasswordVisualTransformation() },
                onValueChange = { viewModel.setConfirmPassword(it) },
                colors = TextFieldDefaults.colors( focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent),
                singleLine = true,
                trailingIcon = {
                    IconButton(modifier = Modifier.width(30.dp).height(30.dp), onClick = {
                        isVisibleCoPassword=!isVisibleCoPassword
                    }){
                        Icon(
                            modifier = Modifier.padding(4.dp),
                            painter = painterResource( if(isVisibleCoPassword) R.drawable.icon_open_eye else R.drawable.icon_eye_close), contentDescription = null
                        )
                    }
                })

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(checked = terms, onCheckedChange = {viewModel.setTerms(it)})

                Row {
                    Text(text = "Yes, I agree with ")
                    Text(text = "term & condition.", color = colorResource(R.color.green_dark))
                }
            }
        }

        MyButton(text = "Signup", true) {

        }

        Text("OR",modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
        OutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(15), onClick ={

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
        TextButton(onClick = {},modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally),) {
            Text("Already have an account ? ")
            Text("Login here", color = colorResource(R.color.blue))
        }

    }

}

@Preview(showBackground = true)
@Composable
fun SignupPreview() {
    StudyRoomTheme {
        SignupScreen(navHostController = rememberNavController())
    }
}
package com.application.studyroom.auth_ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.application.studyroom.R
import com.application.studyroom.ui.StudyRoomTheme
import com.application.studyroom.ui.theme.ButtonText
import com.application.studyroom.ui.theme.MyButton


@Composable
fun LoginScreen(modifier: Modifier = Modifier, navHostController: NavHostController) {
    val context = LocalContext.current

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

        Text(modifier = Modifier.padding(start = 2.dp), text = "Continue with chapter", color = colorResource(
            R.color.green_dark)
        )
        Text(
            text = "Login to your Account",
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
                value = "", onValueChange = { },
                colors = TextFieldDefaults.colors( focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent)
            )
            TextField(
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(6.dp)),
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

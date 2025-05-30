package com.application.studyroom.auth_ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.application.studyroom.R
import com.application.studyroom.ui.StudyRoomTheme
import com.application.studyroom.ui.theme.MyButton

@Composable
fun BoardingScreen(modifier: Modifier = Modifier, navHostController: NavHostController) {

    Column(modifier = modifier.fillMaxSize().padding(all = 12.dp).padding(bottom = 12.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)) {

        Column(modifier = Modifier.weight(weight = 1f).fillMaxWidth().
        padding(top = 40.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {

            Text(text = "Welcome to", color = colorResource(R.color.green_dark))
            Text(text = "StudyRoom",
                modifier = Modifier.padding(top = 8.dp),
                color = colorResource(R.color.title),
                fontSize = 36.sp, fontWeight = FontWeight.W700)

            Image(
                modifier = Modifier.fillMaxWidth().padding(top = 20.dp, start = 40.dp, end = 40.dp, bottom = 5.dp),
                painter = painterResource(R.drawable.art_laptop_man),
                contentDescription = ""
            )

            Text(text = stringResource(R.string.hero_text_boarding_screen),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.W600,
                modifier = Modifier.padding(16.dp))
        }

        MyButton(text = "Create an Account",true) {
            navHostController.navigate(SignupScreen)
        }

        MyButton(text = "Login with Email", false) {
            navHostController.navigate(LoginScreen)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun SplashScreen() {
    StudyRoomTheme {
        BoardingScreen(navHostController = rememberNavController())
    }
}
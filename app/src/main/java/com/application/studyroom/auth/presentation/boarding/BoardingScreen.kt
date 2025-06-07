package com.application.studyroom.auth.presentation.boarding

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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.application.studyroom.R
import com.application.studyroom.auth.components.LoginScreen
import com.application.studyroom.auth.components.SignupScreen
import com.application.studyroom.ui.StudyRoomTheme
import com.application.studyroom.ui.theme.MyButton

@Composable
fun BoardingScreen(modifier: Modifier = Modifier, navHostController: NavHostController) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 18.dp, vertical = 16.dp)
            .padding(bottom = 22.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(top = 60.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Welcome text with lighter weight for better hierarchy
            Text(
                text = "Welcome to",
                color = colorResource(R.color.green_dark),
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.5.sp
            )

            // App name with strong emphasis
            Text(
                text = "StudyRoom",
                modifier = Modifier.padding(top = 4.dp, bottom = 8.dp),
                color = colorResource(R.color.title),
                fontSize = 40.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = (-0.5).sp,
                lineHeight = 44.sp
            )

            // Hero image with better spacing
            Image(
                modifier = Modifier
                    .fillMaxWidth(0.8f) // More controlled width
                    .padding(vertical = 32.dp),
                painter = painterResource(R.drawable.art_laptop_man),
                contentDescription = "Person studying with laptop illustration",
                contentScale = ContentScale.Fit
            )

            // Description text with better readability
            Text(
                text = stringResource(R.string.hero_text_boarding_screen),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                color = colorResource(R.color.gray), // Assuming you have this
                modifier = Modifier.padding(horizontal = 8.dp),
                letterSpacing = 0.1.sp
            )
        }

        // Button section with better spacing
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // Primary action button
            MyButton(
                text = "Create Account", // Shorter, more direct
                isPrimary = true
            ) {
                navHostController.navigate(SignupScreen)
            }

            // Secondary action button
            MyButton(
                text = "Sign In", // More common terminology
                isPrimary = false
            ) {
                navHostController.navigate(LoginScreen)
            }
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
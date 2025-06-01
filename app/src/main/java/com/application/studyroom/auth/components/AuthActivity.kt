package com.application.studyroom.auth.components

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.application.studyroom.auth.presentation.login.LoginScreen
import com.application.studyroom.auth.presentation.register.SignupScreen
import com.application.studyroom.auth.presentation.boarding.BoardingScreen
import com.application.studyroom.ui.StudyRoomTheme
import kotlinx.serialization.Serializable

class AuthActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StudyRoomTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val controller = rememberNavController()
                    val viewModel = ViewModelProvider(this)[AuthViewModel::class.java]

                    NavHost(controller,
                        startDestination = BoardingScreen,
                        ) {
                        composable<BoardingScreen> {
                            viewModel.setCurrentScreen(CurrentScreen.BOARDING_SCREEN)
                            BoardingScreen(
                                modifier = Modifier.padding(innerPadding).fillMaxSize(),controller)
                        }

                        composable<SignupScreen> {
                            viewModel.setCurrentScreen(CurrentScreen.SIGNUP_SCREEN)
                            SignupScreen(
                                modifier = Modifier.padding(innerPadding).fillMaxSize(), controller, viewModel)
                        }
                        composable<LoginScreen> {
                            viewModel.setCurrentScreen(CurrentScreen.LOGIN_SCREEN)
                            LoginScreen(
                                modifier = Modifier.padding(innerPadding).fillMaxSize(), controller)
                        }
                    }
                }
            }
        }
    }
}

@Serializable
object BoardingScreen

@Serializable
object SignupScreen

@Serializable
object LoginScreen
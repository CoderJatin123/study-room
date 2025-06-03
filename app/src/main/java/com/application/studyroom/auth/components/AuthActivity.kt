package com.application.studyroom.auth.components

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
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
import com.application.studyroom.main.components.MainActivity
import com.application.studyroom.auth.domain.AuthRepository
import com.application.studyroom.auth.presentation.login.LoginScreen
import com.application.studyroom.auth.presentation.register.SignupScreen
import com.application.studyroom.auth.presentation.boarding.BoardingScreen
import com.application.studyroom.auth.presentation.login.LoginViewModel
import com.application.studyroom.auth.presentation.register.SignupViewModel
import com.application.studyroom.ui.StudyRoomTheme
import kotlinx.serialization.Serializable

class AuthActivity : ComponentActivity() {
    override fun onStart() {
        super.onStart()

        if(AuthRepository.currentUser!=null){
            onAuthCompleted()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        //Log.d("TAG", "onCreate: "+AuthRepository.currentUser)
        setContent {
            StudyRoomTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val controller = rememberNavController()

                    NavHost(controller,
                        startDestination = BoardingScreen,
                        ) {
                        composable<BoardingScreen> {
                            BoardingScreen(
                                modifier = Modifier
                                    .padding(innerPadding)
                                    .fillMaxSize(),controller)
                        }

                        composable<SignupScreen> {
                            val signupViewModel = ViewModelProvider(LocalActivity.current as AuthActivity)[SignupViewModel::class.java]
                            SignupScreen(
                                modifier = Modifier
                                    .padding(innerPadding)
                                    .fillMaxSize(), signupViewModel)
                        }
                        composable<LoginScreen> {
                            val loginViewModel = ViewModelProvider(LocalActivity.current as AuthActivity)[LoginViewModel::class.java]
                            LoginScreen(
                                modifier = Modifier
                                    .padding(innerPadding)
                                    .fillMaxSize(),loginViewModel)
                        }
                    }
                }
            }
        }
    }

    fun onAuthCompleted(){
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}

@Serializable
object BoardingScreen

@Serializable
object SignupScreen

@Serializable
object LoginScreen
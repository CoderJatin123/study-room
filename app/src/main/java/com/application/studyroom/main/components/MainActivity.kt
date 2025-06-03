package com.application.studyroom.main.components

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.application.studyroom.R
import com.application.studyroom.main.presentation.announcements.AnnouncementsScreen
import com.application.studyroom.main.presentation.components.CurrentScreen
import com.application.studyroom.main.presentation.components.DetailedDrawer
import com.application.studyroom.main.presentation.components.FabWithDropdown
import com.application.studyroom.main.presentation.components.navigateToScreen
import com.application.studyroom.main.presentation.home.HomeScreen
import com.application.studyroom.room.create.components.CreateRoomActivity
import com.application.studyroom.ui.StudyRoomTheme
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        setContent {
            MainScreen(viewModel,this)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: MainViewModel = MainViewModel(), activity: MainActivity? =null) {

    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState (initialValue = DrawerValue.Closed)
    val currentScreen by  viewModel._state
    val navHostController = rememberNavController()

    StudyRoomTheme {
        DetailedDrawer(drawerState, currentScreen, onSelect = {
          navigateToScreen(it,navHostController)

        }) {
            Scaffold(
                floatingActionButton = {
                    if(currentScreen==CurrentScreen.HOME){
                        FabWithDropdown(onCreateRoom = {
                            activity?.startActivity(
                                Intent(activity.baseContext,CreateRoomActivity::class.java)
                            )
                        }, onJoinRoom = {

                        })
                    }else{

                    }
                },
                modifier = Modifier.fillMaxSize(),
                topBar = {
                    TopAppBar(
                        title = {
                            Text(text = "Study Room")
                        },
                        navigationIcon = {
                            IconButton(onClick = {
                                scope.launch {
                                    drawerState.open()
                                }
                            }) {
                                Icon(
                                    painter = painterResource(R.drawable.baseline_menu_24),
                                    tint = Color.White,
                                    contentDescription = "Navigation bar"
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = colorResource(R.color.blue),
                            titleContentColor = Color.White
                        )
                    )
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                ){
                    NavHost(
                        navController = navHostController,
                        startDestination = HomeScreen
                    ){

                      composable<HomeScreen> {
                          viewModel.setCurrentState(CurrentScreen.HOME)
                          HomeScreen()
                        }
                        composable<AnnouncementsScreen> {
                            viewModel.setCurrentState(CurrentScreen.ANNOUNCEMENTS)
                          AnnouncementsScreen()
                        }
                      }
                }
            }
        }
    }
}

@Serializable
object HomeScreen

@Serializable
object AnnouncementsScreen

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    MainScreen()
}
package com.application.studyroom.main.components

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.application.studyroom.R
import com.application.studyroom.main.components.ui.theme.StudyRoomTheme
import com.application.studyroom.main.presentation.announcements.Announcement
import com.application.studyroom.main.presentation.announcements.AnnouncementsScreen
import kotlinx.coroutines.launch

class AnnouncementsActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val roomName = intent.getStringExtra("roomName") ?: "Unknown"

        setContent {
            StudyRoomTheme {
                Scaffold(modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(text = roomName, fontSize = 24.sp)
                            },
                            navigationIcon = {
                                IconButton(onClick = {
                                    finish()
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.ArrowBack,
                                        tint = Color.White,
                                        contentDescription = "Navigation bar"
                                    )
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = colorResource(R.color.green_dark),
                                titleContentColor = Color.White
                            )
                        )
                    }) { innerPadding ->
                    val list = listOf(
                        Announcement(
                            id = "1",
                            title = "Welcome to StudyRoom!",
                            description = "Hello everyone! Welcome to our new StudyRoom. This is where we'll share important updates, assignments, and resources throughout the semester. Please make sure to check this space regularly for announcements.",
                            adminName = "Dr. Sarah Johnson",
                            adminPhotoUrl = null,
                            createdAt = System.currentTimeMillis() - 3600000, // 1 hour ago
                            attachments = null
                        ),
                        Announcement(
                            id = "2",
                            title = "Assignment 1 Due Date Extended",
                            description = "Due to the technical issues we experienced last week, I'm extending the deadline for Assignment 1 to Friday, March 15th at 11:59 PM. Please submit your work through the portal.",
                            adminName = "Prof. Michael Chen",
                            adminPhotoUrl = null,
                            createdAt = System.currentTimeMillis() - 86400000, // 1 day ago
                            attachments = null
                        ),
                        Announcement(
                            id = "3",
                            title = "Mid-term Exam Schedule",
                            description = "The mid-term examination will be held on March 20th, 2024 from 2:00 PM to 4:00 PM in Room 301. Please bring your student ID and calculator. No mobile phones allowed during the exam.",
                            adminName = "Dr. Emily Rodriguez",
                            adminPhotoUrl = null,
                            createdAt = System.currentTimeMillis() - 172800000, // 2 days ago
                            attachments = null
                        )
                    )
                    AnnouncementsScreen(list,"MyRoom", innerPadding)
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    StudyRoomTheme {
        Greeting("Android")
    }
}
package com.application.studyroom.main.presentation.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.application.studyroom.R
import com.application.studyroom.auth.domain.AuthRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun DetailedDrawer(
    drawerState: DrawerState,
    currentScreen: CurrentScreen,
    onSelect: (CurrentScreen)->Unit,
    onLogout: ()-> Unit,
    content: @Composable () -> Unit
) {
    val scope = rememberCoroutineScope()
    val user = AuthRepository.getSignedInUser()
    Log.d("TAG", "DetailedDrawer: ")

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
                Column(
                    modifier = Modifier
                        .padding(end = 4.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Spacer(Modifier.height(12.dp))

                    Row(verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(start = 18.dp)) {
                        Image(
                            modifier = Modifier.size(34.dp).clip(shape = CircleShape),
                            painter = rememberAsyncImagePainter(user?.profilePictureUrl),
                            contentDescription = "App Logo" )
                        Text(
                            text = user?.username ?: "Unknown",
                            modifier = Modifier
                                .padding(vertical = 16.dp)
                                .padding(start = 8.dp),
                            style = MaterialTheme.typography.titleLarge
                        )
                    }

                    HorizontalDivider(
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    DrawerItem(title = "Rooms",
                        isActive = currentScreen==CurrentScreen.HOME,
                        Icons.Outlined.Home,
                        onClick = {
                            onSelect(CurrentScreen.HOME)
                            scope.launch {
                                delay(150)
                                drawerState.close()
                            }
                        })

                    DrawerItem(title = "Announcements",
                        isActive = currentScreen==CurrentScreen.ANNOUNCEMENTS,
                        Icons.Outlined.Notifications,
                        onClick = {
                            onSelect(CurrentScreen.ANNOUNCEMENTS)
                            scope.launch {
                                delay(150)
                                drawerState.close()
                            }
                        })

                    DrawerItem(title = "Settings",
                        isActive = currentScreen==CurrentScreen.SETTINGS,
                        Icons.Outlined.Settings,
                        onClick = {
                            onSelect(CurrentScreen.SETTINGS)
                            scope.launch {
                                delay(150)
                                drawerState.close()
                            }
                        })

                    DrawerLogout(
                        onClick = {
                            onLogout()
                        })
                }
            }
        },
        drawerState = drawerState
    ){
        content()
    }

}
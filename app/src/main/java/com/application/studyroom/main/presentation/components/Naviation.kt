package com.application.studyroom.main.presentation.components

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.application.studyroom.main.components.AnnouncementsScreen
import com.application.studyroom.main.components.HomeScreen

// This function should be called when user clicks navigation buttons
fun navigateToScreen(
    targetScreen: CurrentScreen,
    navHostController: NavHostController
) {
    when(targetScreen) {
        CurrentScreen.HOME -> {
            navHostController.navigate(HomeScreen) {
                // Clear backstack up to start destination (Home)
                popUpTo(navHostController.graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        }

        CurrentScreen.ANNOUNCEMENTS -> {
            navHostController.navigate(AnnouncementsScreen) {
                // Clear backstack up to start destination
                popUpTo(navHostController.graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        }

        CurrentScreen.SETTINGS -> {
//            navHostController.navigate(SettingsScreen) {
//                popUpTo(navHostController.graph.findStartDestination().id) {
//                    saveState = true
//                }
//                launchSingleTop = true
//                restoreState = true
//            }
        }

        CurrentScreen.LOADING -> {
//            // Usually loading screens don't need backstack management
//            // But if needed:
//            navHostController.navigate(LoadingScreen) {
//                launchSingleTop = true
//            }
        }
    }
}
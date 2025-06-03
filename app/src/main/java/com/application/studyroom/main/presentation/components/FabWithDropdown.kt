package com.application.studyroom.main.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FabWithDropdown(
    onCreateRoom: () -> Unit,
    onJoinRoom: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

    Box {
        FloatingActionButton(
            modifier = Modifier.padding(bottom = 24.dp, end = 24.dp),
            onClick = { showMenu = true }
        ) {
            Icon(Icons.Default.Add, "Options")
        }

        DropdownMenu(
            expanded = showMenu,
            onDismissRequest = { showMenu = false }
        ) {
            DropdownMenuItem(
                text = { Text("Create Room") },
                onClick = {
                    onCreateRoom()
                    showMenu = false
                },
                leadingIcon = { Icon(Icons.Default.Add, null) }
            )
            DropdownMenuItem(
                text = { Text("Join Room") },
                onClick = {
                    onJoinRoom()
                    showMenu = false
                },
                leadingIcon = { Icon(Icons.Default.ExitToApp, null) }
            )
        }
    }
}
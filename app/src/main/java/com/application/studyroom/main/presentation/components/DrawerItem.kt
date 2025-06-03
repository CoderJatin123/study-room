package com.application.studyroom.main.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun DrawerItem(title: String, isActive: Boolean,imageVector: ImageVector, onClick: () -> Unit) {
    NavigationDrawerItem(
        modifier = Modifier.padding(end = 12.dp),
        label = { Text(title) },
        selected = isActive,
        icon = { Icon(imageVector, contentDescription = null) },
        onClick = onClick,
        shape = RoundedCornerShape(
            topStart = 0.dp,
            bottomStart = 0.dp,
            topEnd = 32.dp,
            bottomEnd = 32.dp
        )
    )
}
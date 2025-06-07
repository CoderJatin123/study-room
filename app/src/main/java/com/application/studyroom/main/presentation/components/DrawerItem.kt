package com.application.studyroom.main.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.application.studyroom.R

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
@Composable
fun DrawerLogout( onClick: () -> Unit) {
    NavigationDrawerItem(
        modifier = Modifier.padding(end = 12.dp),
        label = { Text("Logout", color = Color.Red) },
        selected = false,
        icon = { Icon(
            tint = Color.Red,
             painter = painterResource(R.drawable.logout_svgrepo_com)
            , contentDescription = null) },
        onClick = onClick,
        shape = RoundedCornerShape(
            topStart = 0.dp,
            bottomStart = 0.dp,
            topEnd = 32.dp,
            bottomEnd = 32.dp
        )
    )
}
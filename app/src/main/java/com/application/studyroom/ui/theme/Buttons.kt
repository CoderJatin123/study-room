package com.application.studyroom.ui.theme

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.application.studyroom.R

@Composable
fun MyButton(text: String, isPrimary: Boolean, onClick: () -> Unit) {
    if (isPrimary) {
        Button(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(15), onClick = onClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(R.color.blue),
                contentColor = Color.White,
            )
        ) {
            ButtonText(text)
        }
    } else {
        OutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(15), onClick = onClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = colorResource(R.color.gray)
            ),
            border = BorderStroke(1.dp, colorResource(R.color.gray))
        ) {
            ButtonText(text)
        }
    }
}

@Composable
fun ButtonText(text: String) {
    Text(
        text = text,
        fontSize = 16.sp,
        fontWeight = FontWeight.W600,
        modifier = Modifier.padding(top = 7.dp, bottom = 7.dp)
    )
}
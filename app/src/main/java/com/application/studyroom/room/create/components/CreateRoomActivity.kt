package com.application.studyroom.room.create.components

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.application.studyroom.R
import com.application.studyroom.ui.StudyRoomTheme
import com.application.studyroom.ui.theme.MyButton
import kotlinx.coroutines.launch

class CreateRoomActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CreateRoomScreen(this)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateRoomScreen(activity: CreateRoomActivity? = null) {

    StudyRoomTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = {
                        Text(text = "Create Room")
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            activity!!.finish()
                        }) {
                            Icon(Icons.Default.ArrowBack, "Back", tint = Color.White)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = colorResource(R.color.blue),
                        titleContentColor = Color.White
                    )
                )
            }) { innerPadding ->
            Box(modifier = Modifier
                .padding(innerPadding)
                .padding(all = 12.dp)) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(6.dp))
                            .onFocusChanged { focusState ->
//                                    if (isEmailFocused && !focusState.isFocused) {
//                                        viewModel.validateEmail()
//                                    }
//                                    isEmailFocused = focusState.isFocused
                            },
                        label = {
                            Text(text = "Room name")
                        },
                        value = "",
                        placeholder = {
                            Text("Enter room name")
                        },
                        onValueChange = {
                            //viewModel.setEmail(it.trim())
                        },
                        supportingText = null,
                    )
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(6.dp))
                            .onFocusChanged { focusState ->
//                                    if (isEmailFocused && !focusState.isFocused) {
//                                        viewModel.validateEmail()
//                                    }
//                                    isEmailFocused = focusState.isFocused
                            },
                        label = {
                            Text(text = "Description")
                        },
                        value = "",
                        minLines = 2,
                        placeholder = {
                            Text("Enter description")
                        },
                        onValueChange = {
                            //viewModel.setEmail(it.trim())
                        },
                        supportingText = null,
                    )

                    Spacer(modifier = Modifier.padding(vertical = 1.dp))

                    MyButton(text = "Create",
                        isPrimary = true) {

                    }

                    Spacer(modifier = Modifier.padding(vertical = 1.dp))

                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(6.dp)),
                        label = {
                            Text(text = "Room Code")
                        },
                        readOnly = true,
                        value = "eg.A1B2C3",
                        enabled = false,
                        onValueChange = {},
                        supportingText = {
                            Text("Copy to clipboard")
                        },
                        trailingIcon = {
                            IconButton(
                                modifier = Modifier
                                    .width(30.dp)
                                    .height(30.dp),
                                onClick = {

                                }) {
                                Icon(painter = painterResource(R.drawable.baseline_content_copy_24),
                                    modifier = Modifier.padding(4.dp),
                                   contentDescription = null
                                )
                            }
                        }
                    )



//                        if (emailError != null) {
//                            Text(
//                                text = emailError.toString(),
//                                color = Color.Red,
//                                style = MaterialTheme.typography.bodySmall,
//                                modifier = Modifier.padding(start = 4.dp),
//                                fontSize = 14.sp
//                            )
//                        }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreateRoomScreenPreview() {
    CreateRoomScreen()
}
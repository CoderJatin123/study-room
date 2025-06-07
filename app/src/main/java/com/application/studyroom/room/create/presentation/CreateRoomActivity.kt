package com.application.studyroom.room.create.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.application.studyroom.R
import com.application.studyroom.room.create.components.CreateRoomViewModel
import com.application.studyroom.ui.StudyRoomTheme
import com.application.studyroom.ui.theme.MyButton

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
fun CreateRoomScreen(
    activity: CreateRoomActivity? = null,
    viewModel: CreateRoomViewModel = CreateRoomViewModel()
) {
    val roomTitle: String by viewModel.roomTitle.collectAsState()
    val roomDesc by viewModel.roomDesc.collectAsState()
    val roomTitleError by viewModel.roomTitleError.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val roomCode by viewModel.generatedRoomCode.collectAsState()

    var isTitleFocused by remember { mutableStateOf(false) }
    var isDescFocused by remember { mutableStateOf(false) }

    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    StudyRoomTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = { Text(text = "Create room") },
                    navigationIcon = {
                        IconButton(onClick = { activity?.finish() }) {
                            Icon(
                                Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = colorResource(R.color.green_dark),
                        titleContentColor = Color.White
                    )
                )
            }
        ) { innerPadding ->

            Box(modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 18.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    Text(
                        modifier = Modifier.padding(top = 6.dp),
                        text = "Create a Study Room",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = colorResource(R.color.title)
                        )
                    )

                    OutlinedTextField(
                        value = roomTitle,
                        onValueChange = { viewModel.setRoomTitle(it) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .onFocusChanged {
                                if (isTitleFocused && !it.isFocused) {
                                    viewModel.validateTitle()
                                }
                                isTitleFocused = it.isFocused
                            },
                        label = { Text("Room Title") },
                        placeholder = { Text("Enter room title") },
                        singleLine = true,
                        isError = roomTitleError != null,
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = colorResource(R.color.green_dark),
                            errorBorderColor = MaterialTheme.colorScheme.error
                        )
                    )

                    if (roomTitleError != null) {
                        Text(
                            text = roomTitleError ?: "",
                            color = Color.Red,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    OutlinedTextField(
                        value = roomDesc,
                        onValueChange = { viewModel.setRoomDesc(it) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .onFocusChanged {
                                isDescFocused = it.isFocused
                            },
                        label = { Text("Room Description") },
                        placeholder = { Text("Enter room description") },
                        maxLines = 4,
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = colorResource(R.color.green_dark)
                        )
                    )

                    OutlinedTextField(
                        value = roomCode ?: "(auto generated)",
                        onValueChange = {  },
                        modifier = Modifier
                            .fillMaxWidth(),
                        label = { Text("Room code :") },
                        placeholder = { Text("Room Code") },
                        maxLines = 4,
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = colorResource(R.color.green_dark)),
                        supportingText = {
                            Text("You can copy and share the code")
                        },
                        readOnly = true,
                        trailingIcon = {
                            IconButton(
                                modifier = Modifier
                                    .width(30.dp)
                                    .height(30.dp),
                                onClick = {
                                    clipboardManager.setText(AnnotatedString(roomCode ?: ""))
                                }) {
                                Icon(
                                    modifier = Modifier.padding(4.dp),
                                    painter = painterResource(R.drawable.baseline_content_copy_24),
                                    contentDescription = null
                                )
                            }
                        }
                    )

                    MyButton(text = "Create Room", true) {
                        isTitleFocused = true
                        isDescFocused = true
                        if(viewModel.generatedRoomCode.value==null){
                            viewModel.createRoom {
                                if(viewModel.generatedRoomCode.value==null){
                                    Toast.makeText(activity!!.baseContext, "Failed to create room", Toast.LENGTH_SHORT).show()
                                }else{
                                    Toast.makeText(activity!!.baseContext, "Room created successfully", Toast.LENGTH_SHORT).show()
                                    activity.finish()
                                }
                            }
                        }
                    }


                }

                if (isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
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
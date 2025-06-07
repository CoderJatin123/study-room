package com.application.studyroom.room.join.presentation

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.application.studyroom.R
import com.application.studyroom.room.join.components.JoinRoomViewModel
import com.application.studyroom.ui.StudyRoomTheme
import com.application.studyroom.ui.theme.MyButton

class JoinRoomActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
           JoinRoomScreen(activity = this@JoinRoomActivity)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JoinRoomScreen(
    activity: JoinRoomActivity? = null,
    viewModel: JoinRoomViewModel = JoinRoomViewModel()
) {

    val roomCode by viewModel.roomCode.collectAsState()
    var isCodeFocused by remember { mutableStateOf(false) }
    val roomCodeError by viewModel.roomCodeError.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()


    StudyRoomTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = { Text(text = "Join room") },
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
                        text = "Join room",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = colorResource(R.color.title)
                        )
                    )



                    OutlinedTextField(
                        value = roomCode ?: "",
                        onValueChange = { newValue ->
                            // Format the input to uppercase and remove spaces/special chars
                            val formatted = newValue.replace(Regex("[^A-Za-z0-9]"), "").uppercase()
                            if (formatted.length <= 6) { // Limit to 6 characters
                                viewModel.setCode(formatted)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .onFocusChanged {
                                if (isCodeFocused && !it.isFocused) {
                                    viewModel.validateRoomCode()
                                }
                                isCodeFocused = it.isFocused
                            },
                        label = { Text("Room Code") },
                        placeholder = { Text("e.g., A2B1C3") },
                        singleLine = true,
                        isError = roomCodeError != null,
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = colorResource(R.color.green_dark),
                            errorBorderColor = MaterialTheme.colorScheme.error
                        ),
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Characters,
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done
                        ),
                        visualTransformation = RoomCodeVisualTransformation(), // Optional: adds formatting
                        supportingText = {
                            if (roomCodeError != null) {
                                Text(
                                    modifier = Modifier.padding(top = 6.dp),
                                    text = roomCodeError ?: "",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontWeight = FontWeight.Normal,
                                        color = MaterialTheme.colorScheme.error
                                    )
                                )
                            } else {
                                Text(
                                    modifier = Modifier.padding(top = 6.dp),
                                    text = "Enter 6-character room code (letters & numbers)",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontWeight = FontWeight.Normal,
                                        color = colorResource(R.color.title)
                                    )
                                )
                            }
                        },
                        trailingIcon = {
                            if (roomCode?.isNotEmpty() == true) {
                                IconButton(onClick = { viewModel.setCode("") }) {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "Clear",
                                        tint = colorResource(R.color.title)
                                    )
                                }
                            }
                        }
                    )

                    MyButton(text = "Join Room", true) {
                        viewModel.joinRoom(){
                            Toast.makeText(activity!!.baseContext, ""+it, Toast.LENGTH_SHORT).show()
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
fun JoinRoomPreview() {
    StudyRoomTheme {
        JoinRoomScreen()
    }
}

// Optional: Custom VisualTransformation for better formatting
class RoomCodeVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = if (text.text.length >= 6) text.text.substring(0..5) else text.text
        var formatted = ""

        for (i in trimmed.indices) {
            formatted += trimmed[i]
            // Add space after every 2 characters for better readability
            if (i % 2 == 1 && i < trimmed.length - 1) {
                formatted += " "
            }
        }

        val numberOffsetTranslator = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                return when {
                    offset <= 0 -> 0
                    offset <= 2 -> offset
                    offset <= 4 -> offset + 1
                    offset <= 6 -> offset + 2
                    else -> formatted.length
                }
            }

            override fun transformedToOriginal(offset: Int): Int {
                return when {
                    offset <= 0 -> 0
                    offset <= 2 -> offset
                    offset <= 3 -> 2 // Handle the space after first pair
                    offset <= 5 -> offset - 1
                    offset <= 6 -> 4 // Handle the space after second pair
                    offset <= 8 -> offset - 2
                    else -> trimmed.length
                }
            }
        }

        return TransformedText(AnnotatedString(formatted), numberOffsetTranslator)
    }
}
package com.application.studyroom.ui.activity

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.application.studyroom.BaseActivity
import com.application.studyroom.R
import com.application.studyroom.databinding.ActivityCreateRoomBinding
import com.application.studyroom.ui.state.UiState
import com.application.studyroom.ui.viewmodel.CreateRoomsViewModel
import com.application.studyroom.utils.RESULT_OK_CREATE_ROOM
import com.application.studyroom.utils.resetErrorHint
import com.application.studyroom.utils.setErrorHint
import com.application.studyroom.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CreateRoomActivity : BaseActivity() {
    private lateinit var binding: ActivityCreateRoomBinding
    private val roomsViewModel: CreateRoomsViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViews()
        initClickListeners()
        observeCreateRoomState()
    }

    override fun onBaseBackPressed() {
        if(roomsViewModel.creteRoomState == UiState.Loading){
            showToast("Please wait for the room to be created")
        }
    }

    private fun observeCreateRoomState() {
        lifecycleScope.launch {
            roomsViewModel.creteRoomState.collectLatest { state ->
                binding.apply {
                    btnCreateRoom.isEnabled = state != UiState.Loading
                    edtRoomTitle.isEnabled = state != UiState.Loading
                    edtRoomDescription.isEnabled = state != UiState.Loading
                    btnCreateRoom.text =
                        if (state == UiState.Loading) "Creating..." else getString(R.string.create_room)
                    tlRoomCode.isEnabled = state is UiState.Success

                    if (state is UiState.Success) {
                        tlRoomCode.editText?.setText(state.data)
                        showToast("Room created successfully")
                        setResult(RESULT_OK_CREATE_ROOM)
                    } else if (state is UiState.Error) {
                        showToast("Failed to create room: ${state.error}")
                    }
                }
            }
        }
    }

    private fun initClickListeners() {
        binding.apply {
            btnCreateRoom.setOnClickListener {
                checkAndCreateRoom()
            }

            edtRoomTitle.addTextChangedListener {
                if (tlRoomTitle.isErrorEnabled) {
                    val name = binding.edtRoomTitle.text.toString().trim()
                    if (name.isNotEmpty()) {
                        if (name.length < 5)
                            tlRoomTitle.setErrorHint("The size of room name must be greater than 5")
                        else
                            tlRoomTitle.resetErrorHint()
                    } else {
                        tlRoomTitle.setErrorHint("This is required field")
                    }
                }
            }

            tlRoomCode.setEndIconOnClickListener {
                val text = binding.edtRoomCode.text.toString()
                val clipboard =
                    getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("Room Code", text)
                clipboard.setPrimaryClip(clip)
                showToast("Room code copied")
            }
        }
    }

    private fun initViews() {
        enableEdgeToEdge()
        setContentView(ActivityCreateRoomBinding.inflate(layoutInflater).also { binding = it }.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun checkAndCreateRoom() {
        val name = binding.edtRoomTitle.text.toString().trim()
        if (name.isNotEmpty()) {
            if (name.length < 5)
                binding.tlRoomTitle.setErrorHint("The size of room name must be greater than 5")
            else {
                roomsViewModel.createRoom(name, binding.edtRoomDescription.text.toString().trim())
            }
        } else {
            binding.tlRoomTitle.setErrorHint("This is required field")
        }
    }
}
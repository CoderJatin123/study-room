package com.application.studyroom.ui.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.application.studyroom.BaseActivity
import com.application.studyroom.databinding.ActivityJoinRoomBinding
import com.application.studyroom.ui.state.UiState
import com.application.studyroom.ui.viewmodel.JoinRoomsViewModel
import com.application.studyroom.utils.RESULT_OK_JOIN_ROOM
import com.application.studyroom.utils.resetErrorHint
import com.application.studyroom.utils.setErrorHint
import com.application.studyroom.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class JoinRoomActivity : BaseActivity() {
    private lateinit var binding: ActivityJoinRoomBinding
    private val roomsViewModel: JoinRoomsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViews()
        initClicks()
        observeJoinRoomState()
    }

    private fun initClicks() {
        binding.apply {
            // Clear error when user starts typing
            edtRoomCode.addTextChangedListener {
                tlRoomCode.error = null
                tlRoomCode.isErrorEnabled = false
            }

            btnJoin.setOnClickListener {
                val roomCode = edtRoomCode.text.toString().trim()

                // Validate room code
                if (roomCode.isEmpty()) {
                    tlRoomCode.error = "Room code is required"
                    tlRoomCode.isErrorEnabled = true
                    return@setOnClickListener
                }

                if (roomCode.length != 6) {
                    tlRoomCode.error = "Room code must be exactly 6 characters"
                    tlRoomCode.isErrorEnabled = true
                    return@setOnClickListener
                }

                // Clear any previous errors
                tlRoomCode.error = null
                tlRoomCode.isErrorEnabled = false

                // Call join room
                roomsViewModel.joinRoom(roomCode.uppercase())
            }
        }
    }

    private fun observeJoinRoomState() {
        lifecycleScope.launch {
            roomsViewModel.joinRoomState.collectLatest { state ->
                binding.apply {
                    btnJoin.text = if (state == UiState.Loading) "Joining..." else "Join Room"
                    btnJoin.isEnabled = state != UiState.Loading
                    edtRoomCode.isEnabled = state != UiState.Loading

                    if (state is UiState.Success) {
                        tlRoomCode.resetErrorHint()
                        showToast("Successfully joined the room!")
                        roomsViewModel.resetJoinRoomState()
                        setResult(RESULT_OK_JOIN_ROOM)
                        finish()
                    } else if (state is UiState.Error) {
                        roomsViewModel.resetJoinRoomState()
                        tlRoomCode.setErrorHint(state.error)
                    }
                }
            }
        }
    }

    override fun onBaseBackPressed() {
        super.onBaseBackPressed()
    }

    private fun initViews() {
        enableEdgeToEdge()
        setContentView(ActivityJoinRoomBinding.inflate(layoutInflater).also { binding = it }.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
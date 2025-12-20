package com.application.studyroom.ui.activity

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.application.studyroom.BaseActivity
import com.application.studyroom.databinding.ActivityJoinRoomBinding
import com.application.studyroom.ui.state.UiState
import com.application.studyroom.ui.viewmodel.RoomsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class JoinRoomActivity : BaseActivity() {
    private lateinit var binding: ActivityJoinRoomBinding
    private val roomsViewModel: RoomsViewModel by viewModels()

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
                when (state) {
                    is UiState.Loading -> {
                        binding.btnJoin.isEnabled = false
                        binding.btnJoin.text = "Joining..."
                    }

                    is UiState.Success -> {
                        binding.btnJoin.isEnabled = true
                        binding.btnJoin.text = "Join Room"
                        Toast.makeText(
                            this@JoinRoomActivity,
                            "Successfully joined the room!",
                            Toast.LENGTH_SHORT
                        ).show()
                        roomsViewModel.resetJoinRoomState()
                        setResult(RESULT_OK)
                        finish()
                    }

                    is UiState.Error -> {
                        binding.btnJoin.isEnabled = true
                        binding.btnJoin.text = "Join Room"
                        binding.tlRoomCode.error = state.error
                        binding.tlRoomCode.isErrorEnabled = true
                        roomsViewModel.resetJoinRoomState()
                    }

                    is UiState.Initial -> {
                        binding.btnJoin.isEnabled = true
                        binding.btnJoin.text = "Join Room"
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
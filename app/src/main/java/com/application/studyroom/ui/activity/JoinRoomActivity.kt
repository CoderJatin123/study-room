package com.application.studyroom.ui.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.application.studyroom.BaseActivity
import com.application.studyroom.databinding.ActivityJoinRoomBinding

class JoinRoomActivity : BaseActivity() {
    private lateinit var binding: ActivityJoinRoomBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViews()
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
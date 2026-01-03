package com.application.studyroom.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.application.studyroom.BaseActivity
import com.application.studyroom.data.model.Announcement
import com.application.studyroom.data.model.Room
import com.application.studyroom.data.model.getRoom
import com.application.studyroom.data.model.toJson
import com.application.studyroom.databinding.ActivityAnnouncementsViewBinding
import com.application.studyroom.ui.adapter.AnnouncementAdapter
import com.application.studyroom.ui.state.UiState
import com.application.studyroom.ui.viewmodel.AnnouncementsViewModel
import com.application.studyroom.utils.RESULT_OK_CREATE_ANNOUNCEMENT
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AnnouncementsViewActivity : BaseActivity() {
    private var announcementAdapter = AnnouncementAdapter()
    private lateinit var room: Room
    private lateinit var binding: ActivityAnnouncementsViewBinding
    private val announcementsViewModel: AnnouncementsViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getData()
    }

    private fun getData() {
        intent.getStringExtra("room")?.let { roomJson ->
            if (roomJson.isEmpty()) {
                finish()
            } else {
                try {
                    roomJson.getRoom()?.let {
                        room = it
                        initViews()
                        initObservers()
                        announcementsViewModel.getAnnouncements(room)
                    } ?: finish()
                } catch (e: Exception) {
                    finish()
                }
            }
        } ?: finish()
    }

    private fun initObservers() {
        lifecycleScope.launch {
            announcementsViewModel.announcementsListState.collectLatest {
                when (it) {
                    is UiState.Error -> {

                    }

                    UiState.Initial -> {

                    }

                    UiState.Loading -> {

                    }

                    is UiState.Success<List<Announcement>> -> {
                        announcementAdapter.update(it.data)
                    }
                }
            }
        }
    }

    private fun initClickListeners() {
        binding.apply {
            fab.setOnClickListener {
                createAnnouncementActivityLauncher.launch(
                    Intent(
                        this@AnnouncementsViewActivity,
                        CreateAnnouncementActivity::class.java
                    ).apply {
                        putExtra("room", room.toJson().toString())
                    })
            }
        }
    }

    private fun initViews() {
        enableEdgeToEdge()
        setContentView(
            ActivityAnnouncementsViewBinding.inflate(layoutInflater).also { binding = it }.root
        )
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.apply {
            supportActionBar?.title = room.name
            rvAnnouncements.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(this@AnnouncementsViewActivity)
                adapter = announcementAdapter
            }
        }
        initClickListeners()
    }

    private val createAnnouncementActivityLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode in listOf(RESULT_OK_CREATE_ANNOUNCEMENT)) {
                announcementsViewModel.getAnnouncements(room)
            }
        }
}
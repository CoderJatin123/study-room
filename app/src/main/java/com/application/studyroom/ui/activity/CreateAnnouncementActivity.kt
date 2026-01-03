package com.application.studyroom.ui.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.application.studyroom.BaseActivity
import com.application.studyroom.R
import com.application.studyroom.data.model.Room
import com.application.studyroom.data.model.getRoom
import com.application.studyroom.databinding.ActivityCraeteAnnouncementBinding
import com.application.studyroom.ui.state.UiState
import com.application.studyroom.ui.viewmodel.AnnouncementsViewModel
import com.application.studyroom.utils.RESULT_OK_CREATE_ANNOUNCEMENT
import com.application.studyroom.utils.resetErrorHint
import com.application.studyroom.utils.setErrorHint
import com.application.studyroom.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CreateAnnouncementActivity : BaseActivity() {

    private lateinit var binding: ActivityCraeteAnnouncementBinding
    private val announcementsViewModel: AnnouncementsViewModel by viewModels()
    private lateinit var room: Room

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
                    } ?: finish()
                } catch (e: Exception) {
                    finish()
                }
            }
        } ?: finish()
    }

    override fun onBaseBackPressed() {
        if (announcementsViewModel.createAnnouncementsListState.value == UiState.Loading) {
            showToast("Please wait for the announcement to be created")
        } else {
            super.onBaseBackPressed()
        }
    }

    private fun initObservers() {
        lifecycleScope.launch {
            announcementsViewModel.createAnnouncementsListState.collectLatest { state ->
                binding.apply {
                    edtDesc.isEnabled = state != UiState.Loading
                    edtTitle.isEnabled = state != UiState.Loading

                    if (state is UiState.Success) {
                        showToast("Announcement created successfully")
                        setResult(RESULT_OK_CREATE_ANNOUNCEMENT)
                        finish()
                    } else if (state is UiState.Error) {
                        showToast("Failed to create announcement: ${state.error}")
                    }
                }
            }
        }
    }

    private fun initViews() {
        enableEdgeToEdge()
        setContentView(
            ActivityCraeteAnnouncementBinding.inflate(layoutInflater).also { binding = it }.root
        )
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.apply {
            room.let {
                supportActionBar?.title = room.name
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_create_announcement, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_done -> {
                onPostClicked()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun onPostClicked() {
        checkAndCreateAnnouncement()
    }

    private fun checkAndCreateAnnouncement() {
        // edtDesc has hint "Title" in XML
        val subject = binding.edtDesc.text.toString().trim()
        // edtTitle has hint "Description" in XML
        val description = binding.edtTitle.text.toString().trim()

        if (subject.isNotEmpty()) {
            if (subject.length < 5) {
                binding.tilDesc.setErrorHint("The size of title must be greater than 5")
            } else {
                binding.tilDesc.resetErrorHint()
                announcementsViewModel.createAnnouncement(room.code!!, subject, description)
            }
        } else {
            binding.tilDesc.setErrorHint("This is required field")
        }
    }
}
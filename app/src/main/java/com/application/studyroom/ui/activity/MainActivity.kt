package com.application.studyroom.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.widget.PopupMenu
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.application.studyroom.R
import com.application.studyroom.custome.BaseActivity
import com.application.studyroom.databinding.ActivityMainBinding
import com.application.studyroom.databinding.NavHeaderMainBinding
import com.application.studyroom.domain.repository.AuthRepository
import com.application.studyroom.ui.state.UiState
import com.application.studyroom.ui.viewmodel.AuthViewModel
import com.application.studyroom.ui.viewmodel.HomeViewModel
import com.application.studyroom.utils.RESULT_OK_CREATE_ROOM
import com.application.studyroom.utils.RESULT_OK_JOIN_ROOM
import com.application.studyroom.utils.startNewActivity
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private val homeViewModel: HomeViewModel by viewModels()
    @Inject
    lateinit var authViewModel: AuthViewModel

    @Inject
    lateinit var authRepository: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        if (authRepository.isUserAvailable() == null) {
            startNewActivity<AuthActivity> { }
            finish()
        }

        setContentView(binding.root)

        binding.appBarMain.fab.setOnClickListener { view ->
            val popMenu = PopupMenu(this@MainActivity, view)

            popMenu.apply {
                menu.add(0, 1, 0, getString(R.string.create_room))
                menu.add(0, 2, 1, getString(R.string.join_room))
                setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        1 -> {
                            createOrJoinRoomContract.launch(
                                Intent(
                                    this@MainActivity,
                                    CreateRoomActivity::class.java
                                )
                            )
                            true
                        }

                        2 -> {
                            createOrJoinRoomContract.launch(
                                Intent(
                                    this@MainActivity,
                                    JoinRoomActivity::class.java
                                )
                            )
                            true
                        }

                        else -> false
                    }
                }

                show()
            }

        }

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_rooms, R.id.nav_announcements, R.id.nav_settings
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        setUpDrawerViews(navView.getHeaderView(0))
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_logout -> onLogout()

            R.id.action_settings -> {

            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun onLogout() {
        lifecycleScope.launch {
            authRepository.logout {
                authViewModel.reset()
                startNewActivity<AuthActivity> { }
                finish()
            }
        }
    }

    private fun setUpDrawerViews(headerView: View) {
        val headerBinding = NavHeaderMainBinding.bind(headerView)
        authRepository.isUserAvailable()?.let {
            Glide.with(this).load(it.photoUrl).into(headerBinding.imageView)
            headerBinding.tvTitle.text = it.displayName ?: "-"
            headerBinding.tvSubTitle.text = it.email ?: "-"
        }
    }

    private val createOrJoinRoomContract =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode in listOf(RESULT_OK_CREATE_ROOM, RESULT_OK_JOIN_ROOM)) {
                homeViewModel.refresh()
            }
        }
}
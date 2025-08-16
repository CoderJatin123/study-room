package com.application.studyroom.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.application.studyroom.R
import com.application.studyroom.databinding.ActivityAuthBinding
import com.application.studyroom.domain.repository.AuthRepository
import com.application.studyroom.utils.startNewActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {

    @Inject
    lateinit var authRepository: AuthRepository

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAuthBinding.inflate(layoutInflater)

        lifecycleScope.launch {
            if (authRepository.isUserAvailable() == null)
                setContentView(binding.root)
            else onAuthComplete()
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_auth)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    fun onAuthComplete() {
        startNewActivity(MainActivity::class.java)
    }

    fun setLoading(isLoading: Boolean){
        binding.progressbar.isVisible = isLoading
    }
}
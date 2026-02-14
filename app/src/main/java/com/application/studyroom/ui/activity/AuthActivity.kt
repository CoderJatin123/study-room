package com.application.studyroom.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.application.studyroom.R
import com.application.studyroom.custome.BaseActivity
import com.application.studyroom.databinding.ActivityAuthBinding
import com.application.studyroom.network.NetworkHelper
import com.application.studyroom.ui.state.UiState
import com.application.studyroom.ui.state.UiState.Loading
import com.application.studyroom.ui.viewmodel.AuthViewModel
import com.application.studyroom.utils.startNewActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AuthActivity : BaseActivity() {

    @Inject
    lateinit var networkHelper: NetworkHelper

    @Inject
    lateinit var authViewModel: AuthViewModel
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityAuthBinding
    private val googleAuthUiClient by lazy {
        authViewModel.authRepository.getGoogleAuthClient(this)
    }

    override fun onBaseBackPressed() {
        if (authViewModel.loginUiState.value !is Loading && authViewModel.signupUiState.value !is Loading)
            super.onBaseBackPressed()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ActivityAuthBinding.inflate(layoutInflater).also { binding = it }.root)
        if (authViewModel.isUserLoggedIn())
            onAuthComplete()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_auth)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    fun onAuthComplete() {
        startNewActivity<MainActivity> {}
        finish()
    }

    fun setLoading(isLoading: Boolean) {
        if (::binding.isInitialized) {
            binding.progressbar.isVisible = isLoading
        }
    }

    fun onGoogleAuth() {
        authViewModel.setState(Loading)
        if (!networkHelper.isInternetAvailable()) {
            authViewModel.setState(UiState.Error("No internet connection was found"))
            return
        }
        authViewModel.setState(UiState.Initial)
        binding.progressbar.isVisible = false
        lifecycleScope.launch {
            googleAuthUiClient.signInWithIntent(onSuccess = {
                authViewModel.setState(UiState.Success(it))
                onAuthComplete()
            }, onFailed = {
                authViewModel.setState(UiState.Error("Authentication failed. Please try again later."))
            })
        }
    }
}
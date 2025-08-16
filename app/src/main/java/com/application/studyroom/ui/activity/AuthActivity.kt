package com.application.studyroom.ui.activity

import android.app.Activity
import android.os.Bundle
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.application.studyroom.R
import com.application.studyroom.databinding.ActivityAuthBinding
import com.application.studyroom.ui.state.UiState
import com.application.studyroom.ui.viewmodel.AuthViewModel
import com.application.studyroom.utils.startNewActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {


    private val authViewModel: AuthViewModel by viewModels()

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityAuthBinding
    private val googleAuthUiClient by lazy {
        authViewModel.authRepository.getGoogleAuthClient(this)
    }

    private val signInLauncher = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            lifecycleScope.launch {
                authViewModel.setState(UiState.Loading)
                val signInResult = googleAuthUiClient.signInWithIntent(
                    intent = result.data ?: return@launch
                )
                signInResult.data?.let {
                    authViewModel.setState(UiState.Success(it))
                    onAuthComplete()
                } ?: authViewModel.setState(UiState.Error("Authentication failed. Please try again later."))

            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (authViewModel.isUserLoggedIn())
            onAuthComplete()
        else
            setContentView(ActivityAuthBinding.inflate(layoutInflater).also { binding = it }.root)

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_auth)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    fun onAuthComplete() {
        startNewActivity(MainActivity::class.java)
        finish()
    }

    fun setLoading(isLoading: Boolean) {
        binding.progressbar.isVisible = isLoading
    }

    fun onGoogleAuth() {
        authViewModel.setState(UiState.Loading)
        lifecycleScope.launch {
            val signInIntentSender = googleAuthUiClient.signIn()
            authViewModel.setState(UiState.Initial)
            binding.progressbar.isVisible=false
            signInLauncher.launch(
                IntentSenderRequest.Builder(
                    signInIntentSender ?: return@launch
                ).build()
            )
        }
    }
}
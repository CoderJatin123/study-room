package com.application.studyroom.di

import android.content.Context
import com.application.studyroom.domain.implementations.FirebaseAuthentication
import com.application.studyroom.domain.repository.AuthRepository
import com.application.studyroom.ui.viewmodel.AuthViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AuthModule {

    @Singleton
    @Provides
    fun providesAuthRepository(): AuthRepository  = FirebaseAuthentication()

    @Singleton
    @Provides
    fun providesAuthViewModel(authRepository: AuthRepository) = AuthViewModel(authRepository)
}
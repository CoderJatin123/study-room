package com.application.studyroom.di

import android.content.Context
import com.application.studyroom.domain.implementations.RoomRepositoryImpl
import com.application.studyroom.domain.repository.RoomRepository
import com.application.studyroom.network.NetworkHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RoomModule {
    @Singleton
    @Provides
    fun providesRoomRepository(networkHelper: NetworkHelper): RoomRepository = RoomRepositoryImpl(networkHelper)

    @Singleton
    @Provides
    fun providesNetworkHelper(@ApplicationContext context: Context) : NetworkHelper = NetworkHelper(context)
}
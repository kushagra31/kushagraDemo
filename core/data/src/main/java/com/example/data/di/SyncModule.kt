package com.example.data.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class SyncModule {

//    @Binds
//    abstract fun bindsUsersRepository(
//        impl: HoldingsRepositoryImpl
//    ): HoldingsRepository

//    @Binds
//    internal abstract fun bindsSyncStatusMonitor(
//        syncStatusMonitor: UserSyncManager,
//    ): SyncManager
}
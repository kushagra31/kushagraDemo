package com.example.data.di

import com.example.data.repository.HoldingsRepository
import com.example.data.repository.HoldingsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class DataModule {

    @Binds
    abstract fun bindsStocksRepository(
        impl: HoldingsRepositoryImpl
    ): HoldingsRepository
}
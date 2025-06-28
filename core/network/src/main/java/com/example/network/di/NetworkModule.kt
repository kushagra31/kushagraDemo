package com.example.network.di

import com.example.model.Constants
import com.example.network.ConnectivityManager
import com.example.network.NetworkMonitor
import com.example.network.StocksApiService
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideGsonConverterFactory()
            : GsonConverterFactory = GsonConverterFactory.create()

    @Singleton
    @Provides
    fun provideHttpClient(
    ): OkHttpClient {
        val okHttpClientBuilder = OkHttpClient.Builder()
        return okHttpClientBuilder.build()
    }

    @Singleton
    @Provides
    fun provideRetrofitB(
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .client(OkHttpClient())
            .baseUrl(Constants.API_URL)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    @Singleton
    @Provides
    fun provideStocksApiService(
        retrofit: Retrofit
    ): StocksApiService {
        return retrofit.create(
            StocksApiService::class.java
        )
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkMonitorModule {
    @Binds
    internal abstract fun bindsNetworkMonitor(
        networkMonitor: ConnectivityManager,
    ): NetworkMonitor
}
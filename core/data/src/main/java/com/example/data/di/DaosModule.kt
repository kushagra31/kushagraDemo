package com.example.data.di

import android.content.Context
import androidx.room.Room
import com.example.data.UserDao
import com.example.data.UserDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DaosModule {
    @Provides
    fun providesUsersDao(
        database: UserDatabase,
    ): UserDao = database.userDao()


    @Provides
    @Singleton
    fun providesUserDatabase(
        @ApplicationContext context: Context,
    ): UserDatabase = Room.databaseBuilder(
        context,
        UserDatabase::class.java,
        "user-database",
    ).build()
}



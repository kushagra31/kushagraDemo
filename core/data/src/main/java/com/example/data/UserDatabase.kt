package com.example.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.model.models.UserEntity

/**
 * Database class with a singleton Instance object.
 */
@Database(entities = [UserEntity::class], version = 1, exportSchema = false)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}
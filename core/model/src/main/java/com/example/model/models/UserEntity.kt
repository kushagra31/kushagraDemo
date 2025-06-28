package com.example.model.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val job: String,
    val isSynced: Boolean = false
)

fun UserEntity.asExternalModel() = User(
    name = name,
    job = job
)
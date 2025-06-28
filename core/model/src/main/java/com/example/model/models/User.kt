package com.example.model.models

data class User (
    val name: String,
    val job: String,
)

fun User.asEntity() = UserEntity(
    name = name,
    job = job,
)

fun User.asUsersRequest() = UserRequest(
    name = name,
    job = job,
)
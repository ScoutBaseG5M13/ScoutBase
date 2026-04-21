package com.empresa.scoutbase.model.user

data class User(
    val id: String,
    val username: String,
    val role: String,
    val name: String,
    val surname: String,
    val email: String
)

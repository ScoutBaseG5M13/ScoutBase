package com.empresa.scoutbase.model.player

data class PlayerCreateRequest(
    val name: String,
    val surname: String,
    val age: Int,
    val email: String,
    val number: Int,
    val teamId: String,
    val position: String,
    val priority: Int
)

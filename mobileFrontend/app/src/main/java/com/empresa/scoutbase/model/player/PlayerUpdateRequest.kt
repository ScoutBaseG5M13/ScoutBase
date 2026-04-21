package com.empresa.scoutbase.model.player

data class PlayerUpdateRequest(
    val id: String,
    val name: String,
    val surname: String,
    val age: Int,
    val email: String,
    val number: Int,
    val position: String,
    val priority: Int
)

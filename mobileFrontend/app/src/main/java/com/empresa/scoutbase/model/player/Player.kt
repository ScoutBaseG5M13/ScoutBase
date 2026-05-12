package com.empresa.scoutbase.model.player

data class Player(
    val id: String,
    val teamId: String,
    val name: String,
    val surname: String,
    val birthYear: Int,
    val email: String,
    val number: Int,
    val position: String,
    val priority: Int,
    val stats: Any?
)


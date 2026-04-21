package com.empresa.scoutbase.model.team

data class Team(
    val id: String,
    val name: String,
    val category: String,
    val subCategory: String,
    val trainer: String?,        // pot ser null si backend no l’envia
    val secondTrainer: String?,  // opcional
    val scouters: List<String>?, // opcional
    val players: List<String>?   // només IDs, no calen encara
)

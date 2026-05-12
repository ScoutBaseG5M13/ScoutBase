package com.empresa.scoutbase.model.UserTeam

data class UserTeam(
    val id: String,
    val name: String,
    val category: String,
    val subcategory: String,
    val userClub: String,
    val trainer: String?,
    val secondTrainer: String?,
    val scouters: List<String>?
)


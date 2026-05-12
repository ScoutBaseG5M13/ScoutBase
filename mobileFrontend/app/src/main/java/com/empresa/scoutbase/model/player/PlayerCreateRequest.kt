// com/empresa/scoutbase/model/player/PlayerCreateRequest.kt
package com.empresa.scoutbase.model.player

data class PlayerCreateRequest(
    val name: String,
    val surname: String,
    val birthYear: Int,
    val email: String,
    val number: Int,
    val position: String,
    val priority: Int
)



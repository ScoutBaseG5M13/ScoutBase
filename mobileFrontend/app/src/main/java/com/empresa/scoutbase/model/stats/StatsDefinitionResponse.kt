package com.empresa.scoutbase.model.stats

data class StatsDefinitionResponse(
    val success: Boolean,
    val message: String,
    val data: List<StatDefinition>,
    val sessionId: String,
    val timestamp: String
)



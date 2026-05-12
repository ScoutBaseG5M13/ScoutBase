package com.empresa.scoutbase.ui.components.stats

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun RatingStars(score: Double) {
    val fullStars = score.toInt()
    val hasHalf = (score - fullStars) >= 0.5
    val totalStars = 5

    Row {
        repeat(fullStars) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }

        if (hasHalf && fullStars < totalStars) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
            )
        }

        val remaining = totalStars - fullStars - if (hasHalf) 1 else 0
        repeat(remaining) {
            Icon(
                imageVector = Icons.Filled.StarBorder,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}




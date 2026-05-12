package com.empresa.scoutbase.screen.stats

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.empresa.scoutbase.model.stats.PlayerStat
import com.empresa.scoutbase.ui.components.stats.RatingStars
import com.empresa.scoutbase.viewmodel.stats.PlayerReportViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerReportScreen(
    token: String,
    playerId: String,
    viewModel: PlayerReportViewModel,
    onBack: () -> Unit
) {
    val stats by viewModel.stats.collectAsState()
    val loading by viewModel.loading.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadPlayerStats(token, playerId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Informe del jugador") },
                navigationIcon = {
                    IconButton(onClick = { onBack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Tornar enrere"
                        )
                    }
                }
            )
        }
    ) { padding ->

        if (loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }

        if (stats.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("Aquest jugador no té informe encara.")
            }
            return@Scaffold
        }

        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {

            // ⭐ MITJANA GLOBAL
            item {
                Text("Valoració global", style = MaterialTheme.typography.titleLarge)
                RatingStars(viewModel.globalAverage())
                Text("${String.format("%.2f", viewModel.globalAverage())}/5")
                Spacer(modifier = Modifier.height(24.dp))
            }

            // ⭐ MITJANES PER CAPACITAT
            val groups = listOf("OFENSIVO", "DEFENSIVO", "FISICO", "MENTAL")

            items(groups) { type ->
                val avg = viewModel.averageByType(type)

                Text(type, style = MaterialTheme.typography.titleMedium)
                RatingStars(avg)
                Text("${String.format("%.2f", avg)}/5")

                Spacer(modifier = Modifier.height(16.dp))

                // 🏅 Millors 2 stats
                val best = viewModel.bestTwo(type)
                if (best.isNotEmpty()) {
                    Text("Millors stats:", style = MaterialTheme.typography.bodyLarge)
                    best.forEach { stat ->
                        Text("🥇 ${stat.code} — ${stat.name}: ${stat.value}")
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }

            // 📋 LLISTA COMPLETA DE STATS
            item {
                Text("Totes les stats", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(12.dp))
            }

            items(stats) { stat ->
                StatRow(stat)
            }
        }
    }
}

@Composable
fun StatRow(stat: PlayerStat) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text("${stat.code} — ${stat.name}")
        RatingStars(stat.value.toDouble())
        Text("${stat.value}/5")
    }
}





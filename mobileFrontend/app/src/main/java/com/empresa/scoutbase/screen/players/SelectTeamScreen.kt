package com.empresa.scoutbase.screen.players

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.empresa.scoutbase.model.player.TeamData
import com.empresa.scoutbase.ui.theme.AzulPetroleo
import com.empresa.scoutbase.ui.theme.FondoClaro
import com.empresa.scoutbase.viewmodel.players.SelectTeamViewModel

@Composable
fun SelectTeamScreen(
    clubId: String,
    token: String,
    onBack: () -> Unit,
    onSelectTeam: (String) -> Unit,
    onCreateTeam: () -> Unit
) {
    val viewModel: SelectTeamViewModel = viewModel()

    LaunchedEffect(clubId, token) {
        viewModel.loadTeams(token, clubId)
    }

    val teams by viewModel.teams.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onCreateTeam() },
                containerColor = AzulPetroleo,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Crear Team")
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(FondoClaro)
                .padding(padding)
                .padding(20.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 20.dp)
            ) {
                IconButton(onClick = { onBack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver",
                        tint = AzulPetroleo
                    )
                }

                Text(
                    text = "Selecciona un Equipo",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = AzulPetroleo
                )
            }

            when {
                loading -> Text("Cargando equipos...", fontSize = 20.sp, color = AzulPetroleo)
                error != null -> Text("Error: $error", fontSize = 20.sp, color = Color.Red)
                else -> {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        items(teams) { team ->
                            TeamCard(team = team, onClick = { onSelectTeam(team.id) })
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TeamCard(team: TeamData, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(6.dp, RoundedCornerShape(16.dp))
            .background(Color.White, RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(20.dp)
    ) {
        Text(
            text = team.name,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = AzulPetroleo
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text("Categoría: ${team.category}", fontSize = 16.sp)
        Text("Subcategoría: ${team.subcategory}", fontSize = 16.sp)
    }
}

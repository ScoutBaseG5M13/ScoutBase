package com.empresa.scoutbase.screen.players

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import com.empresa.scoutbase.model.player.Player
import com.empresa.scoutbase.ui.theme.AzulPetroleo
import com.empresa.scoutbase.ui.theme.FondoClaro
import com.empresa.scoutbase.ui.theme.TextoPrincipal
import com.empresa.scoutbase.viewmodel.players.PlayerListViewModel

@Composable
fun PlayersListScreen(
    teamId: String,
    username: String,
    token: String,
    onBack: () -> Unit,
    onCreatePlayer: () -> Unit,
    onEditPlayer: (Player) -> Unit
) {
    val viewModel: PlayerListViewModel = viewModel()

    LaunchedEffect(teamId, token) {
        viewModel.loadPlayers(token, teamId)
    }

    val players by viewModel.players.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onCreatePlayer() },
                containerColor = AzulPetroleo,
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "Crear jugador")
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
                    text = "Lista de Jugadores",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = AzulPetroleo
                )
            }

            when {
                loading -> Text("Cargando jugadores...", fontSize = 20.sp, color = AzulPetroleo)
                error != null -> Text("Error: $error", fontSize = 20.sp, color = Color.Red)
                else -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(players) { player ->
                            PlayerCard(
                                player = player,
                                onClick = { onEditPlayer(player) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PlayerCard(player: Player, onClick: (Player) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(player) }
            .shadow(6.dp, RoundedCornerShape(16.dp))
            .background(Color.White, RoundedCornerShape(16.dp))
            .padding(20.dp)
    ) {

        Text(
            text = "${player.name} ${player.surname}",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = AzulPetroleo
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text("Posición: ${player.position}", fontSize = 16.sp, color = TextoPrincipal)
        Text("Edad: ${player.age}", fontSize = 16.sp, color = TextoPrincipal)
        Text("Número: ${player.number}", fontSize = 16.sp, color = TextoPrincipal)
        Text("Prioridad: ${player.priority}", fontSize = 16.sp, color = TextoPrincipal)
    }
}










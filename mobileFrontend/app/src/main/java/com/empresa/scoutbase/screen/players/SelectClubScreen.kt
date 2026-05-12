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
import com.empresa.scoutbase.model.player.ClubData
import com.empresa.scoutbase.ui.theme.AzulPetroleo
import com.empresa.scoutbase.ui.theme.FondoClaro
import com.empresa.scoutbase.viewmodel.players.SelectClubViewModel

@Composable
fun SelectClubScreen(
    userClubId: String,
    token: String,
    onBack: () -> Unit,
    onSelectClub: (String) -> Unit,
    onCreateClub: () -> Unit
) {
    val viewModel: SelectClubViewModel = viewModel()

    LaunchedEffect(userClubId, token) {
        viewModel.loadClubs(token, userClubId)
    }

    val clubs by viewModel.clubs.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoClaro)
            .padding(20.dp)
    ) {

        Column(modifier = Modifier.fillMaxSize()) {

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
                    text = "Selecciona un Club",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = AzulPetroleo
                )
            }

            when {
                loading -> Text("Cargando clubs...", fontSize = 20.sp, color = AzulPetroleo)
                error != null -> Text("Error: $error", fontSize = 20.sp, color = Color.Red)
                else -> {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        items(clubs) { club ->
                            ClubCard(club = club, onClick = { onSelectClub(club.id) })
                        }
                    }
                }
            }
        }

        // FAB para crear club virtual
        FloatingActionButton(
            onClick = { onCreateClub() },
            containerColor = AzulPetroleo,
            contentColor = Color.White,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Crear club")
        }
    }
}

@Composable
fun ClubCard(club: ClubData, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(6.dp, RoundedCornerShape(16.dp))
            .background(Color.White, RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(20.dp)
    ) {
        Text(
            text = club.name,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = AzulPetroleo
        )
    }
}


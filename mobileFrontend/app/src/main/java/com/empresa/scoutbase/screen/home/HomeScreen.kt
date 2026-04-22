package com.empresa.scoutbase.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.empresa.scoutbase.model.team.Team
import com.empresa.scoutbase.ui.theme.*
import com.empresa.scoutbase.viewmodel.home.HomeViewModel

@Composable
fun HomeScreen(
    username: String,
    role: String,
    token: String,
    onLogout: () -> Unit = {},
    onEnterTeam: (String) -> Unit
) {
    val cleanRole = role.removePrefix("ROLE_")

    val roleColor = when (role.uppercase()) {
        "ROLE_ADMIN" -> Color(0xFF9B59B6)
        else -> AzulGrisaceo
    }

    val homeViewModel: HomeViewModel = viewModel()

    LaunchedEffect(Unit) {
        homeViewModel.loadTeams(token)
    }

    val teams by homeViewModel.teams.collectAsState()
    val loading by homeViewModel.loading.collectAsState()
    val error by homeViewModel.error.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = FondoClaro
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(WindowInsets.statusBars.asPaddingValues())
                .padding(24.dp)
        ) {

            // -------------------------
            // HEADER ORDENADO
            // -------------------------
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column {
                    Text(
                        text = "Dashboard",
                        fontSize = 34.sp,
                        fontWeight = FontWeight.Bold,
                        color = AzulPetroleo
                    )
                    Text(
                        text = "Bienvenido, $username",
                        fontSize = 18.sp,
                        color = TextoPrincipal
                    )
                }

                Column(horizontalAlignment = Alignment.End) {

                    Text(
                        text = cleanRole,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = roleColor
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = { onLogout() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFD63031),
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .height(40.dp)
                            .widthIn(min = 140.dp)
                    ) {
                        Text(
                            text = "Cerrar sesión",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            // -------------------------
            // CONTENIDO
            // -------------------------
            when {
                loading -> {
                    Text(
                        text = "Cargando equipos...",
                        fontSize = 20.sp,
                        color = AzulPetroleo
                    )
                }

                error != null -> {
                    Text(
                        text = "Error: $error",
                        fontSize = 20.sp,
                        color = Color.Red
                    )
                }

                else -> {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        teams.forEach { team ->
                            TeamCard(
                                team = team,
                                onEnterScouting = { onEnterTeam(team.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TeamCard(
    team: Team,
    onEnterScouting: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(6.dp, RoundedCornerShape(16.dp))
            .background(Color.White, RoundedCornerShape(16.dp))
            .padding(20.dp)
    ) {

        Text(
            text = team.name,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = AzulPetroleo
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text("Categoría: ${team.category}", fontSize = 18.sp, color = TextoPrincipal)
        Text("Subcategoría: ${team.subCategory}", fontSize = 18.sp, color = TextoPrincipal)

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onEnterScouting(team.id) },
            colors = ButtonDefaults.buttonColors(
                containerColor = AzulPetroleo,
                contentColor = Color.White
            ),
            modifier = Modifier
                .height(45.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "+ Entrar al panel de scouting",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}










package com.empresa.scoutbase.screen.players

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.empresa.scoutbase.model.player.PlayerCreateRequest
import com.empresa.scoutbase.ui.theme.AzulPetroleo
import com.empresa.scoutbase.ui.theme.FondoClaro
import com.empresa.scoutbase.viewmodel.players.PlayerCreateViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePlayerScreen(
    teamId: String,
    token: String,
    onBack: () -> Unit
) {
    val viewModel: PlayerCreateViewModel = viewModel()

    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var birthYearUi by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var number by remember { mutableStateOf("") }
    var priority by remember { mutableStateOf("") }

    // Dropdown de posiciones
    val positions = listOf(
        "PORTERO","LATERAL_DERECHO","LATERAL_IZQUIERDO","DEFENSA_CENTRAL",
        "DEFENSA_CENTRAL_DERECHO","DEFENSA_CENTRAL_IZQUIERDO","CARRILERO_DERECHO",
        "CARRILERO_IZQUIERDO","MEDIOCENTRO","MEDIOCENTRO_DEFENSIVO",
        "MEDIOCENTRO_OFENSIVO","INTERIOR_DERECHO","INTERIOR_IZQUIERDO",
        "EXTREMO_DERECHO","EXTREMO_IZQUIERDO","MEDIAPUNTA",
        "DELANTERO_CENTRO","SEGUNDO_DELANTERO"
    )

    var positionExpanded by remember { mutableStateOf(false) }
    var selectedPosition by remember { mutableStateOf(positions.first()) }

    // Dropdown de años
    val years = (1995..2026).toList().reversed()
    var yearExpanded by remember { mutableStateOf(false) }

    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()
    val success by viewModel.success.collectAsState()

    LaunchedEffect(success) {
        if (success) onBack()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoClaro)
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
                text = "Crear Jugador",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = AzulPetroleo
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = surname,
                onValueChange = { surname = it },
                label = { Text("Apellido") },
                modifier = Modifier.fillMaxWidth()
            )

            // Año de nacimiento como dropdown
            ExposedDropdownMenuBox(
                expanded = yearExpanded,
                onExpandedChange = { yearExpanded = !yearExpanded }
            ) {
                OutlinedTextField(
                    value = birthYearUi,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Año de nacimiento") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(yearExpanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = yearExpanded,
                    onDismissRequest = { yearExpanded = false }
                ) {
                    years.forEach { y ->
                        DropdownMenuItem(
                            text = { Text(y.toString()) },
                            onClick = {
                                birthYearUi = y.toString()
                                yearExpanded = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = number,
                onValueChange = { number = it },
                label = { Text("Número") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = priority,
                onValueChange = { priority = it },
                label = { Text("Prioridad") },
                modifier = Modifier.fillMaxWidth()
            )

            // Dropdown de posición
            ExposedDropdownMenuBox(
                expanded = positionExpanded,
                onExpandedChange = { positionExpanded = !positionExpanded }
            ) {
                OutlinedTextField(
                    value = selectedPosition,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Posición") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(positionExpanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = positionExpanded,
                    onDismissRequest = { positionExpanded = false }
                ) {
                    positions.forEach { pos ->
                        DropdownMenuItem(
                            text = { Text(pos) },
                            onClick = {
                                selectedPosition = pos
                                positionExpanded = false
                            }
                        )
                    }
                }
            }

            if (error != null) {
                Text(text = error ?: "", color = Color.Red)
            }

            Button(
                onClick = {
                    val birthYear = birthYearUi.toIntOrNull() ?: 0

                    val request = PlayerCreateRequest(
                        name = name,
                        surname = surname,
                        birthYear = birthYear,
                        email = email,
                        number = number.toIntOrNull() ?: 0,
                        position = selectedPosition,
                        priority = priority.toIntOrNull() ?: 0
                    )

                    viewModel.createPlayer(token, teamId, request)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AzulPetroleo,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = if (loading) "Guardando..." else "Guardar",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}








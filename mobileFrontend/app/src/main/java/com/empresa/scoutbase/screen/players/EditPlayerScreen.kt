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
import com.empresa.scoutbase.model.player.Player
import com.empresa.scoutbase.model.player.PlayerUpdateRequest
import com.empresa.scoutbase.ui.theme.*
import com.empresa.scoutbase.viewmodel.players.PlayerEditViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPlayerScreen(
    player: Player,
    token: String,
    onBack: () -> Unit
) {
    val viewModel: PlayerEditViewModel = viewModel()

    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()
    val success by viewModel.success.collectAsState()
    val deleted by viewModel.deleted.collectAsState()

    LaunchedEffect(success) {
        if (success) onBack()
    }

    LaunchedEffect(deleted) {
        if (deleted) onBack()
    }

    var name by remember { mutableStateOf(player.name) }
    var surname by remember { mutableStateOf(player.surname) }
    var age by remember { mutableStateOf(player.age.toString()) }
    var email by remember { mutableStateOf(player.email) }
    var number by remember { mutableStateOf(player.number.toString()) }
    var priority by remember { mutableStateOf(player.priority.toString()) }
    var position by remember { mutableStateOf(player.position) }

    val positions = listOf(
        "PORTERO","LATERAL_DERECHO","LATERAL_IZQUIERDO","DEFENSA_CENTRAL",
        "DEFENSA_CENTRAL_DERECHO","DEFENSA_CENTRAL_IZQUIERDO","CARRILERO_DERECHO",
        "CARRILERO_IZQUIERDO","MEDIOCENTRO","MEDIOCENTRO_DEFENSIVO",
        "MEDIOCENTRO_OFENSIVO","INTERIOR_DERECHO","INTERIOR_IZQUIERDO",
        "EXTREMO_DERECHO","EXTREMO_IZQUIERDO","MEDIAPUNTA",
        "DELANTERO_CENTRO","SEGUNDO_DELANTERO"
    )

    var expanded by remember { mutableStateOf(false) }

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
                text = "Editar Jugador",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = AzulPetroleo
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

            OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = surname, onValueChange = { surname = it }, label = { Text("Apellido") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = age, onValueChange = { age = it }, label = { Text("Edad") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = number, onValueChange = { number = it }, label = { Text("Número") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = priority, onValueChange = { priority = it }, label = { Text("Prioridad") }, modifier = Modifier.fillMaxWidth())

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = position,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Posición") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    positions.forEach { pos ->
                        DropdownMenuItem(
                            text = { Text(pos) },
                            onClick = {
                                position = pos
                                expanded = false
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
                    val request = PlayerUpdateRequest(
                        id = player.id,   // ← AÑADIDO
                        name = name,
                        surname = surname,
                        age = age.toIntOrNull() ?: 0,
                        email = email,
                        number = number.toIntOrNull() ?: 0,
                        position = position,
                        priority = priority.toIntOrNull() ?: 0
                    )
                    viewModel.updatePlayer(token, player.id, request)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AzulPetroleo)
            ) {
                Text("Guardar cambios", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = { viewModel.deletePlayer(token, player.id) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text("Eliminar jugador", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}







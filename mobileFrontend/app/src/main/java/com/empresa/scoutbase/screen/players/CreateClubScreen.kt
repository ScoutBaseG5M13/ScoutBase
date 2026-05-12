// com/empresa/scoutbase/screen/players/CreateClubScreen.kt
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
import com.empresa.scoutbase.model.player.ClubCreateRequest
import com.empresa.scoutbase.ui.theme.*
import com.empresa.scoutbase.viewmodel.players.ClubCreateViewModel

@Composable
fun CreateClubScreen(
    userClubId: String,
    token: String,
    onBack: () -> Unit
) {
    val viewModel: ClubCreateViewModel = viewModel()

    var name by remember { mutableStateOf("") }

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
                text = "Crear Club virtual",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = AzulPetroleo
            )
        }

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nombre del club") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        )

        if (error != null) {
            Text(text = error ?: "", color = Color.Red)
        }

        Button(
            onClick = {
                val req = ClubCreateRequest(name = name)
                viewModel.createClub(token, userClubId, req)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AzulPetroleo)
        ) {
            Text(
                if (loading) "Guardando..." else "Crear club",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}



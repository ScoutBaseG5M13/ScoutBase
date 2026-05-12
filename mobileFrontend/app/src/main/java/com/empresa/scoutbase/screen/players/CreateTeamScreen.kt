// com/empresa/scoutbase/screen/players/CreateTeamScreen.kt
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
import com.empresa.scoutbase.model.player.TeamCreateRequest
import com.empresa.scoutbase.ui.theme.*
import com.empresa.scoutbase.viewmodel.players.TeamCreateViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTeamScreen(
    clubId: String,
    token: String,
    onBack: () -> Unit
) {
    val viewModel: TeamCreateViewModel = viewModel()

    var name by remember { mutableStateOf("") }
    var categoryUi by remember { mutableStateOf("") }
    var subcategoryUi by remember { mutableStateOf("") }

    // Lo que ve el usuario
    val categoriesUi = listOf("PREBENJAMÍN", "BENJAMÍN", "ALEVÍN", "INFANTIL", "CADETE", "JUVENIL")

    // Mapa UI -> API
    val categoryApiMap = mapOf(
        "PREBENJAMÍN" to "PREBENJAMIN",
        "BENJAMÍN" to "BENJAMIN",
        "ALEVÍN" to "ALEVIN",
        "INFANTIL" to "INFANTIL",
        "CADETE" to "CADETE",
        "JUVENIL" to "JUVENIL"
    )

    // Subcategorías UI por categoría (ajusta si hace falta)
    val subcategoriesUiMap = mapOf(
        "PREBENJAMÍN" to listOf("SUB-7", "SUB-8"),
        "BENJAMÍN" to listOf("SUB-9", "SUB-10"),
        "ALEVÍN" to listOf("SUB-11", "SUB-12"),
        "INFANTIL" to listOf("SUB-13", "SUB-14"),
        "CADETE" to listOf("SUB-15", "SUB-16"),
        "JUVENIL" to listOf("SUB_SUPERIOR")
    )

    // Mapa UI -> API para subcategorías (quitamos guion)
    fun subcategoryUiToApi(subUi: String): String =
        subUi.replace("-", "") // "SUB-11" -> "SUB11"

    var categoryExpanded by remember { mutableStateOf(false) }
    var subcategoryExpanded by remember { mutableStateOf(false) }

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
                text = "Crear Equipo virtual",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = AzulPetroleo
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre del equipo") },
                modifier = Modifier.fillMaxWidth()
            )

            // Categoría
            ExposedDropdownMenuBox(
                expanded = categoryExpanded,
                onExpandedChange = { categoryExpanded = !categoryExpanded }
            ) {
                OutlinedTextField(
                    value = categoryUi,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Categoría") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(categoryExpanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = categoryExpanded,
                    onDismissRequest = { categoryExpanded = false }
                ) {
                    categoriesUi.forEach { cat ->
                        DropdownMenuItem(
                            text = { Text(cat) },
                            onClick = {
                                categoryUi = cat
                                subcategoryUi = ""
                                categoryExpanded = false
                            }
                        )
                    }
                }
            }

            // Subcategoría
            ExposedDropdownMenuBox(
                expanded = subcategoryExpanded,
                onExpandedChange = {
                    if (categoryUi.isNotEmpty()) {
                        subcategoryExpanded = !subcategoryExpanded
                    }
                }
            ) {
                OutlinedTextField(
                    value = subcategoryUi,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Subcategoría") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(subcategoryExpanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    enabled = categoryUi.isNotEmpty()
                )

                ExposedDropdownMenu(
                    expanded = subcategoryExpanded,
                    onDismissRequest = { subcategoryExpanded = false }
                ) {
                    val subs = subcategoriesUiMap[categoryUi] ?: emptyList()
                    subs.forEach { sub ->
                        DropdownMenuItem(
                            text = { Text(sub) },
                            onClick = {
                                subcategoryUi = sub
                                subcategoryExpanded = false
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
                    val categoryApi = categoryApiMap[categoryUi] ?: ""
                    val subcategoryApi = subcategoryUiToApi(subcategoryUi)

                    val req = TeamCreateRequest(
                        category = categoryApi,
                        name = name,
                        subcategory = subcategoryApi
                    )
                    viewModel.createTeam(token, clubId, req)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AzulPetroleo)
            ) {
                Text(
                    if (loading) "Guardando..." else "Crear equipo",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}




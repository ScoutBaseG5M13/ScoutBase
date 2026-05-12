package com.empresa.scoutbase.screen.stats

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.empresa.scoutbase.viewmodel.stats.CreateReportViewModel
import com.empresa.scoutbase.ui.components.stats.StatGroupSection

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateReportScreen(
    token: String,
    playerId: String,
    viewModel: CreateReportViewModel,
    onSaved: () -> Unit
) {
    val stats by viewModel.stats.collectAsState()
    val values by viewModel.values.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val saved by viewModel.saved.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadStats(token)
    }

    if (saved) {
        onSaved()
    }

    val tabs = listOf("OFENSIVO", "DEFENSIVO", "FISICO", "MENTAL")
    var selectedTab by remember { mutableStateOf(0) }

    Column(modifier = Modifier.fillMaxSize()) {

        ScrollableTabRow(
            selectedTabIndex = selectedTab,
            edgePadding = 8.dp
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(title) }
                )
            }
        }

        val currentType = tabs[selectedTab]
        val filteredStats = stats.filter { it.type == currentType }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {

            StatGroupSection(
                title = currentType,
                stats = filteredStats,
                values = values,
                onValueChange = { code, value ->
                    viewModel.updateValue(code, value)
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { viewModel.saveReport(token, playerId) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar informe")
            }

            if (loading) {
                Spacer(modifier = Modifier.height(16.dp))
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }
        }
    }
}




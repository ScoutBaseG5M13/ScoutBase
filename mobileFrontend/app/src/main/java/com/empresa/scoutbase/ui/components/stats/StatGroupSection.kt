package com.empresa.scoutbase.ui.components.stats

import androidx.compose.foundation.layout.*
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.empresa.scoutbase.model.stats.StatDefinition

@Composable
fun StatGroupSection(
    title: String,
    stats: List<StatDefinition>,
    values: Map<String, Int>,
    onValueChange: (String, Int) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {

        Text(text = title, modifier = Modifier.padding(vertical = 8.dp))

        stats.forEach { stat ->
            Column(modifier = Modifier.padding(vertical = 8.dp)) {

                Text("${stat.code} — ${stat.name}")

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    (0..5).forEach { value ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            RadioButton(
                                selected = values[stat.code] == value,
                                onClick = { onValueChange(stat.code, value) }
                            )
                            Text(text = value.toString())
                        }
                    }
                }
            }
        }
    }
}




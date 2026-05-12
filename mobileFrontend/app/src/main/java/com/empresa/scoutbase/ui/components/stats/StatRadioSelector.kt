package com.empresa.scoutbase.ui.components.stats

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun StatRadioSelector(
    selected: Int,
    onSelect: (Int) -> Unit
) {
    Row(modifier = Modifier.padding(vertical = 4.dp)) {
        (0..5).forEach { value ->
            Row(modifier = Modifier.padding(end = 12.dp)) {
                RadioButton(
                    selected = selected == value,
                    onClick = { onSelect(value) }
                )
                Text(text = value.toString(), modifier = Modifier.padding(start = 4.dp))
            }
        }
    }
}



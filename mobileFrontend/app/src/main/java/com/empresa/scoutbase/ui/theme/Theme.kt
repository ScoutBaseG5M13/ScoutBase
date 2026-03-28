package com.empresa.scoutbase.ui.theme


import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(

    primary = AzulPetroleo,
    secondary = AzulGrisaceo,

    background = FondoClaro,
    surface = FondoTarjetas,

    onPrimary = FondoClaro,
    onSecondary = FondoClaro,
    onBackground = TextoPrincipal,
    onSurface = TextoPrincipal
)

@Composable
fun ScoutbaseTheme(
    content: @Composable () -> Unit
) {

    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}
package com.empresa.scoutbase.screen.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import com.empresa.scoutbase.ui.theme.*

/**
 * Pantalla principal tras el login.
 * Muestra el rol real y permite cerrar sesión.
 */
@Composable
fun HomeScreen(
    username: String,
    role: String,
    onLogout: () -> Unit = {}
) {
    // El backend devuelve ROLE_ADMIN, ROLE_USER...
    val cleanRole = role.removePrefix("ROLE_")

    val roleColor = when (role.uppercase()) {
        "ROLE_ADMIN" -> Color(0xFF9B59B6)
        else -> AzulGrisaceo
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = FondoClaro
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(WindowInsets.statusBars.asPaddingValues())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "Dashboard Scoutbase",
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold,
                color = AzulPetroleo
            )

            Spacer(modifier = Modifier.height(40.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column(horizontalAlignment = Alignment.End) {

                    Text(
                        text = cleanRole,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = roleColor
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = { onLogout() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFD63031),
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .height(48.dp)
                            .widthIn(min = 160.dp)
                    ) {
                        Text(
                            text = "Cerrar sesión",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "Bienvenido, $username",
                fontSize = 30.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextoPrincipal
            )

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}


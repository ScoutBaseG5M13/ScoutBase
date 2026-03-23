package com.empresa.scoutbase.screen.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.empresa.scoutbase.ui.theme.AzulPetroleo
import com.empresa.scoutbase.ui.theme.FondoClaro
import com.empresa.scoutbase.viewmodel.login.LoginViewModel

/**
 * Pantalla de inicio de sesión.
 * Navega a Home cuando el token y el rol han sido obtenidos correctamente.
 */
@Composable
fun LoginScreen(
    onLoginSuccess: (String, String) -> Unit
) {
    // Estados locales para usuario y contraseña
    var usuario by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }

    // ViewModel
    val loginViewModel: LoginViewModel = viewModel()

    // Estados expuestos por el ViewModel
    val loading by loginViewModel.loading.collectAsState()
    val errorServidor by loginViewModel.error.collectAsState()
    val token by loginViewModel.token.collectAsState()
    val role by loginViewModel.role.collectAsState()

    /**
     * Cuando token y role no son nulos, significa que:
     * 1. El login ha sido correcto
     * 2. Se ha obtenido el rol real del backend
     * Entonces se navega a Home.
     */
    LaunchedEffect(token, role) {
        val currentToken = token
        val currentRole = role

        if (currentToken != null && currentRole != null) {
            onLoginSuccess(usuario, currentRole)
        }
    }


    Surface(
        modifier = Modifier.fillMaxSize(),
        color = FondoClaro
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Scoutbase",
                fontSize = 32.sp,
                color = AzulPetroleo
            )

            Spacer(modifier = Modifier.height(40.dp))

            OutlinedTextField(
                value = usuario,
                onValueChange = { usuario = it },
                label = { Text("Usuario") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = contrasena,
                onValueChange = { contrasena = it },
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (errorServidor != null) {
                Text(
                    text = errorServidor ?: "",
                    color = MaterialTheme.colorScheme.error
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (usuario.isBlank()) {
                        loginViewModel.setError("El usuario no puede estar vacío")
                        return@Button
                    }

                    if (contrasena.isBlank()) {
                        loginViewModel.setError("La contraseña no puede estar vacía")
                        return@Button
                    }

                    loginViewModel.login(usuario, contrasena)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Iniciar sesión")
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (loading) {
                CircularProgressIndicator()
            }
        }
    }
}




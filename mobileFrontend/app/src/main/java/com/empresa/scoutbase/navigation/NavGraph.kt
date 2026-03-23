package com.empresa.scoutbase.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.empresa.scoutbase.screen.home.HomeScreen
import com.empresa.scoutbase.screen.login.LoginScreen

/**
 * Define las rutas de navegación de la aplicación.
 */
object Routes {
    const val LOGIN = "login"
    const val HOME = "home"
}

@Composable
fun AppNavGraph(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN
    ) {

        // Pantalla de Login
        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess = { username, role ->
                    navController.navigate(
                        "${Routes.HOME}/$username/$role"
                    ) {
                        // Elimina Login del backstack
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        // Pantalla Home
        composable(
            route = "${Routes.HOME}/{username}/{role}"
        ) { backStackEntry ->

            val username = backStackEntry.arguments?.getString("username") ?: ""
            val role = backStackEntry.arguments?.getString("role") ?: ""

            HomeScreen(
                username = username,
                role = role,
                onLogout = {
                    navController.navigate(Routes.LOGIN) {
                        // Elimina Home del backstack
                        popUpTo(Routes.HOME) { inclusive = true }
                    }
                }
            )
        }
    }
}


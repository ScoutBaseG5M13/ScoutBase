package com.empresa.scoutbase.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.empresa.scoutbase.model.player.Player
import com.empresa.scoutbase.screen.home.HomeScreen
import com.empresa.scoutbase.screen.login.LoginScreen
import com.empresa.scoutbase.screen.players.PlayersListScreen
import com.empresa.scoutbase.screen.players.CreatePlayerScreen
import com.empresa.scoutbase.screen.players.EditPlayerScreen
import com.google.gson.Gson

object Routes {
    const val LOGIN = "login"
    const val HOME = "home"
    const val PLAYERS_LIST = "playersList"
    const val CREATE_PLAYER = "createPlayer"
    const val EDIT_PLAYER = "editPlayer"
}

@Composable
fun AppNavGraph(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN
    ) {

        // LOGIN
        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess = { username, role, token ->
                    val encodedToken = Uri.encode(token)
                    navController.navigate("${Routes.HOME}/$username/$role/$encodedToken") {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        // HOME
        composable(
            route = "${Routes.HOME}/{username}/{role}/{token}",
            arguments = listOf(
                navArgument("username") { type = NavType.StringType },
                navArgument("role") { type = NavType.StringType },
                navArgument("token") { type = NavType.StringType }
            )
        ) { backStackEntry ->

            val username = backStackEntry.arguments?.getString("username") ?: ""
            val role = backStackEntry.arguments?.getString("role") ?: ""
            val token = Uri.decode(backStackEntry.arguments?.getString("token") ?: "")

            HomeScreen(
                username = username,
                role = role,
                token = token,
                onLogout = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.HOME) { inclusive = true }
                    }
                },
                onEnterTeam = { teamId ->
                    val encodedToken = Uri.encode(token)
                    navController.navigate("${Routes.PLAYERS_LIST}/$teamId/$username/$encodedToken")
                }
            )
        }

        // PLAYERS LIST
        composable(
            route = "${Routes.PLAYERS_LIST}/{teamId}/{username}/{token}",
            arguments = listOf(
                navArgument("teamId") { type = NavType.StringType },
                navArgument("username") { type = NavType.StringType },
                navArgument("token") { type = NavType.StringType }
            )
        ) { backStackEntry ->

            val teamId = backStackEntry.arguments?.getString("teamId") ?: ""
            val username = backStackEntry.arguments?.getString("username") ?: ""
            val token = Uri.decode(backStackEntry.arguments?.getString("token") ?: "")

            PlayersListScreen(
                teamId = teamId,
                username = username,
                token = token,
                onBack = { navController.popBackStack() },
                onCreatePlayer = {
                    val encodedToken = Uri.encode(token)
                    navController.navigate("${Routes.CREATE_PLAYER}/$teamId/$encodedToken")
                },
                onEditPlayer = { player ->
                    val encodedToken = Uri.encode(token)
                    val playerJson = Uri.encode(Gson().toJson(player))
                    navController.navigate("${Routes.EDIT_PLAYER}/$playerJson/$encodedToken")
                }
            )
        }

        // CREATE PLAYER
        composable(
            route = "${Routes.CREATE_PLAYER}/{teamId}/{token}",
            arguments = listOf(
                navArgument("teamId") { type = NavType.StringType },
                navArgument("token") { type = NavType.StringType }
            )
        ) { backStackEntry ->

            val teamId = backStackEntry.arguments?.getString("teamId") ?: ""
            val token = Uri.decode(backStackEntry.arguments?.getString("token") ?: "")

            CreatePlayerScreen(
                teamId = teamId,
                token = token,
                onBack = { navController.popBackStack() }
            )
        }

        // EDIT PLAYER
        composable(
            route = "${Routes.EDIT_PLAYER}/{playerJson}/{token}",
            arguments = listOf(
                navArgument("playerJson") { type = NavType.StringType },
                navArgument("token") { type = NavType.StringType }
            )
        ) { backStackEntry ->

            val playerJson = Uri.decode(backStackEntry.arguments?.getString("playerJson") ?: "")
            val player = Gson().fromJson(playerJson, Player::class.java)

            val token = Uri.decode(backStackEntry.arguments?.getString("token") ?: "")

            EditPlayerScreen(
                player = player,
                token = token,
                onBack = { navController.popBackStack() }
            )
        }
    }
}









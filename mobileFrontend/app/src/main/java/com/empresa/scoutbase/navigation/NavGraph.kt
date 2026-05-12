package com.empresa.scoutbase.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.empresa.scoutbase.model.player.Player
import com.empresa.scoutbase.screen.home.HomeScreen
import com.empresa.scoutbase.screen.login.LoginScreen
import com.empresa.scoutbase.screen.players.*
import com.empresa.scoutbase.screen.stats.CreateReportScreen
import com.empresa.scoutbase.screen.stats.PlayerReportScreen
import com.empresa.scoutbase.repository.StatsRepositoryImpl
import com.empresa.scoutbase.viewmodel.stats.CreateReportViewModel
import com.empresa.scoutbase.viewmodel.stats.CreateReportViewModelFactory
import com.empresa.scoutbase.viewmodel.stats.PlayerReportViewModel
import com.empresa.scoutbase.viewmodel.stats.PlayerReportViewModelFactory
import com.google.gson.Gson

object Routes {
    const val LOGIN = "login"
    const val HOME = "home"
    const val SELECT_CLUB = "selectClub"
    const val SELECT_TEAM = "selectTeam"
    const val CREATE_CLUB = "createClub"
    const val CREATE_TEAM = "createTeam"
    const val PLAYERS_LIST = "playersList"
    const val CREATE_PLAYER = "createPlayer"
    const val EDIT_PLAYER = "editPlayer"
    const val CREATE_REPORT = "createReport"
    const val PLAYER_REPORT = "playerReport"
}

@Composable
fun AppNavGraph(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN
    ) {

        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess = { username, role, token, userClubId ->

                    val encodedToken = Uri.encode(token)
                    val encodedClub = Uri.encode(userClubId)

                    navController.navigate(
                        "${Routes.HOME}/$username/$role/$encodedToken/$encodedClub"
                    ) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = "${Routes.HOME}/{username}/{role}/{token}/{userClubId}",
            arguments = listOf(
                navArgument("username") { type = NavType.StringType },
                navArgument("role") { type = NavType.StringType },
                navArgument("token") { type = NavType.StringType },
                navArgument("userClubId") { type = NavType.StringType }
            )
        ) { backStackEntry ->

            val username = backStackEntry.arguments?.getString("username") ?: ""
            val role = backStackEntry.arguments?.getString("role") ?: ""
            val token = Uri.decode(backStackEntry.arguments?.getString("token") ?: "")
            val userClubId = Uri.decode(backStackEntry.arguments?.getString("userClubId") ?: "")

            HomeScreen(
                username = username,
                role = role,
                token = token,
                userClubId = userClubId,
                onLogout = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.HOME) { inclusive = true }
                    }
                },
                onEnterTeam = {
                    val encodedToken = Uri.encode(token)
                    val encodedClub = Uri.encode(userClubId)
                    navController.navigate("${Routes.SELECT_CLUB}/$encodedToken/$encodedClub")
                }
            )
        }

        composable(
            route = "${Routes.SELECT_CLUB}/{token}/{userClubId}",
            arguments = listOf(
                navArgument("token") { type = NavType.StringType },
                navArgument("userClubId") { type = NavType.StringType }
            )
        ) { backStackEntry ->

            val token = Uri.decode(backStackEntry.arguments?.getString("token") ?: "")
            val userClubId = Uri.decode(backStackEntry.arguments?.getString("userClubId") ?: "")

            SelectClubScreen(
                userClubId = userClubId,
                token = token,
                onBack = { navController.popBackStack() },
                onSelectClub = { clubId ->
                    val encodedToken = Uri.encode(token)
                    navController.navigate("${Routes.SELECT_TEAM}/$clubId/$encodedToken")
                },
                onCreateClub = {
                    val encodedToken = Uri.encode(token)
                    val encodedUserClub = Uri.encode(userClubId)
                    navController.navigate("${Routes.CREATE_CLUB}/$encodedUserClub/$encodedToken")
                }
            )
        }

        composable(
            route = "${Routes.CREATE_CLUB}/{userClubId}/{token}",
            arguments = listOf(
                navArgument("userClubId") { type = NavType.StringType },
                navArgument("token") { type = NavType.StringType }
            )
        ) { backStackEntry ->

            val userClubId = Uri.decode(backStackEntry.arguments?.getString("userClubId") ?: "")
            val token = Uri.decode(backStackEntry.arguments?.getString("token") ?: "")

            CreateClubScreen(
                userClubId = userClubId,
                token = token,
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = "${Routes.SELECT_TEAM}/{clubId}/{token}",
            arguments = listOf(
                navArgument("clubId") { type = NavType.StringType },
                navArgument("token") { type = NavType.StringType }
            )
        ) { backStackEntry ->

            val clubId = backStackEntry.arguments?.getString("clubId") ?: ""
            val token = Uri.decode(backStackEntry.arguments?.getString("token") ?: "")

            SelectTeamScreen(
                clubId = clubId,
                token = token,
                onBack = { navController.popBackStack() },
                onSelectTeam = { teamId ->
                    val encodedToken = Uri.encode(token)
                    navController.navigate("${Routes.PLAYERS_LIST}/$teamId/$encodedToken")
                },
                onCreateTeam = {
                    val encodedToken = Uri.encode(token)
                    navController.navigate("${Routes.CREATE_TEAM}/$clubId/$encodedToken")
                }
            )
        }

        composable(
            route = "${Routes.CREATE_TEAM}/{clubId}/{token}",
            arguments = listOf(
                navArgument("clubId") { type = NavType.StringType },
                navArgument("token") { type = NavType.StringType }
            )
        ) { backStackEntry ->

            val clubId = backStackEntry.arguments?.getString("clubId") ?: ""
            val token = Uri.decode(backStackEntry.arguments?.getString("token") ?: "")

            CreateTeamScreen(
                clubId = clubId,
                token = token,
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = "${Routes.PLAYERS_LIST}/{teamId}/{token}",
            arguments = listOf(
                navArgument("teamId") { type = NavType.StringType },
                navArgument("token") { type = NavType.StringType }
            )
        ) { backStackEntry ->

            val teamId = backStackEntry.arguments?.getString("teamId") ?: ""
            val token = Uri.decode(backStackEntry.arguments?.getString("token") ?: "")

            PlayersListScreen(
                teamId = teamId,
                username = "",
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
                onBack = { navController.popBackStack() },
                onCreateReport = { playerId ->
                    val encodedToken = Uri.encode(token)
                    navController.navigate("${Routes.CREATE_REPORT}/$playerId/$encodedToken")
                },
                onViewReport = { playerId ->
                    val encodedToken = Uri.encode(token)
                    navController.navigate("${Routes.PLAYER_REPORT}/$playerId/$encodedToken")
                }
            )
        }

        composable(
            route = "${Routes.CREATE_REPORT}/{playerId}/{token}",
            arguments = listOf(
                navArgument("playerId") { type = NavType.StringType },
                navArgument("token") { type = NavType.StringType }
            )
        ) { backStackEntry ->

            val playerId = backStackEntry.arguments?.getString("playerId") ?: ""
            val token = Uri.decode(backStackEntry.arguments?.getString("token") ?: "")

            val repo = StatsRepositoryImpl()
            val vm: CreateReportViewModel = viewModel(
                factory = CreateReportViewModelFactory(repo)
            )

            CreateReportScreen(
                token = token,
                playerId = playerId,
                viewModel = vm,
                onSaved = { navController.popBackStack() }
            )
        }

        composable(
            route = "${Routes.PLAYER_REPORT}/{playerId}/{token}",
            arguments = listOf(
                navArgument("playerId") { type = NavType.StringType },
                navArgument("token") { type = NavType.StringType }
            )
        ) { backStackEntry ->

            val playerId = backStackEntry.arguments?.getString("playerId") ?: ""
            val token = Uri.decode(backStackEntry.arguments?.getString("token") ?: "")

            val repo = StatsRepositoryImpl()
            val vm: PlayerReportViewModel = viewModel(
                factory = PlayerReportViewModelFactory(repo)
            )

            PlayerReportScreen(
                token = token,
                playerId = playerId,
                viewModel = vm,
                onBack = { navController.popBackStack() }
            )
        }
    }
}

















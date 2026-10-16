package com.example.sshmanager.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.sshmanager.AppContainer
import com.example.sshmanager.ui.servers.ServerEditScreen
import com.example.sshmanager.ui.servers.ServersScreen
import com.example.sshmanager.ui.terminal.TerminalScreen

object Routes {
    const val SERVERS = "servers"
    const val SERVER_EDIT = "server_edit"
    const val TERMINAL = "terminal"

    const val ARG_SERVER_ID = "serverId"
}

@Composable
fun SshManagerNavHost(container: AppContainer) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.SERVERS,
    ) {
        composable(Routes.SERVERS) {
            ServersScreen(
                repository = container.repository,
                onAddServer = {
                    navController.navigate("${Routes.SERVER_EDIT}?${Routes.ARG_SERVER_ID}=-1")
                },
                onEditServer = { id ->
                    navController.navigate("${Routes.SERVER_EDIT}?${Routes.ARG_SERVER_ID}=$id")
                },
                onOpenTerminal = { id ->
                    navController.navigate("${Routes.TERMINAL}/$id")
                },
            )
        }

        composable(
            route = "${Routes.SERVER_EDIT}?${Routes.ARG_SERVER_ID}={${Routes.ARG_SERVER_ID}}",
            arguments = listOf(
                navArgument(Routes.ARG_SERVER_ID) {
                    type = NavType.LongType
                    defaultValue = -1L
                },
            ),
        ) { entry ->
            val serverId = entry.arguments?.getLong(Routes.ARG_SERVER_ID) ?: -1L
            ServerEditScreen(
                repository = container.repository,
                serverId = serverId,
                onNavigateBack = { navController.popBackStack() },
            )
        }

        composable(
            route = "${Routes.TERMINAL}/{${Routes.ARG_SERVER_ID}}",
            arguments = listOf(
                navArgument(Routes.ARG_SERVER_ID) { type = NavType.LongType },
            ),
        ) { entry ->
            val serverId = entry.arguments?.getLong(Routes.ARG_SERVER_ID) ?: 0L
            TerminalScreen(
                repository = container.repository,
                serverId = serverId,
                onNavigateBack = { navController.popBackStack() },
            )
        }
    }
}

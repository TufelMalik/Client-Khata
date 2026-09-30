package com.techquantum.tqdkhata.modules.client_detail.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.techquantum.tqdkhata.modules.client_detail.ui.ClientDetailScreen
import com.techquantum.tqdkhata.modules.clients.viewmodel.ClientViewModel

fun NavGraphBuilder.clientDetailNavGraph(
    viewModel: ClientViewModel,
    navController: NavController
) {
    composable(
        route = ClientDetailRoute.ROUTE,
        arguments = listOf(navArgument(ClientDetailRoute.ARG_CLIENT_ID) { type = NavType.LongType })
    ) { backStackEntry ->
        val clientId = backStackEntry.arguments?.getLong(ClientDetailRoute.ARG_CLIENT_ID) ?: 0L
        ClientDetailScreen(
            clientId = clientId,
            viewModel = viewModel,
            onNavigateBack = { navController.popBackStack() },
            onNavigateToEdit = { id -> navController.navigate("client_form?clientId=$id") }
        )
    }
}

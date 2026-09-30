package com.techquantum.tqdkhata.modules.client_form.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.techquantum.tqdkhata.modules.client_form.ui.AddEditClientScreen
import com.techquantum.tqdkhata.modules.clients.viewmodel.ClientViewModel

fun NavGraphBuilder.clientFormNavGraph(
    viewModel: ClientViewModel,
    navController: NavController
) {
    composable(
        route = ClientFormRoute.ROUTE,
        arguments = listOf(
            navArgument(ClientFormRoute.ARG_CLIENT_ID) {
                type = NavType.LongType
                defaultValue = -1L
            }
        )
    ) { backStackEntry ->
        val clientId = backStackEntry.arguments?.getLong(ClientFormRoute.ARG_CLIENT_ID) ?: -1L
        AddEditClientScreen(
            clientId = if (clientId > 0L) clientId else null,
            viewModel = viewModel,
            onNavigateBack = { navController.popBackStack() }
        )
    }
}

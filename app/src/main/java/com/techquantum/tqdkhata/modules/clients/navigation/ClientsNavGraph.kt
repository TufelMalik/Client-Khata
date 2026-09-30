package com.techquantum.tqdkhata.modules.clients.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.techquantum.tqdkhata.modules.clients.ui.ClientListScreen
import com.techquantum.tqdkhata.modules.clients.viewmodel.ClientViewModel

fun NavGraphBuilder.clientsNavGraph(
    viewModel: ClientViewModel,
    navController: NavController
) {
    composable(ClientsRoute.ROUTE) {
        ClientListScreen(
            viewModel = viewModel,
            onNavigateToAddClient = { navController.navigate("client_form") },
            onNavigateToClientDetail = { id -> navController.navigate("client_detail/$id") },
            onNavigateToReminders = { navController.navigate("reminders") }
        )
    }
}

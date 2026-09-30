package com.techquantum.tqdkhata.modules.reminders.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.techquantum.tqdkhata.modules.clients.viewmodel.ClientViewModel
import com.techquantum.tqdkhata.modules.reminders.ui.RemindersScreen

fun NavGraphBuilder.remindersNavGraph(
    viewModel: ClientViewModel,
    navController: NavController
) {
    composable(RemindersRoute.ROUTE) {
        RemindersScreen(
            viewModel = viewModel,
            onNavigateBack = { navController.popBackStack() },
            onNavigateToClientDetail = { id -> navController.navigate("client_detail/$id") }
        )
    }
}

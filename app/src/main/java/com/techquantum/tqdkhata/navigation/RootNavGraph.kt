package com.techquantum.tqdkhata.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.techquantum.tqdkhata.modules.client_detail.navigation.clientDetailNavGraph
import com.techquantum.tqdkhata.modules.client_form.navigation.clientFormNavGraph
import com.techquantum.tqdkhata.modules.clients.navigation.clientsNavGraph
import com.techquantum.tqdkhata.modules.clients.viewmodel.ClientViewModel
import com.techquantum.tqdkhata.modules.reminders.navigation.remindersNavGraph

fun NavGraphBuilder.rootNavGraph(
    viewModel: ClientViewModel,
    navController: NavController
) {
    clientsNavGraph(viewModel, navController)
    clientFormNavGraph(viewModel, navController)
    clientDetailNavGraph(viewModel, navController)
    remindersNavGraph(viewModel, navController)
}

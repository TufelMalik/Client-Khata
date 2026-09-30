package com.techquantum.tqdkhata.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.techquantum.tqdkhata.modules.clients.viewmodel.ClientViewModel

@Composable
fun AppNavHost(
    navController: NavHostController,
    viewModel: ClientViewModel,
    modifier: Modifier = Modifier,
    pendingClientId: Long? = null,
    onClearPendingClient: () -> Unit = {}
) {
    // Deep link navigation when notification is tapped
    LaunchedEffect(pendingClientId) {
        if (pendingClientId != null && pendingClientId > 0L) {
            navController.navigateToClientDetail(pendingClientId)
            onClearPendingClient()
        }
    }

    NavHost(
        navController = navController,
        startDestination = TQDKhataDestinations.CLIENTS,
        modifier = modifier
    ) {
        rootNavGraph(viewModel, navController)
    }
}

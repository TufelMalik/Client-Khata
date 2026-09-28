package com.techquantum.tqdkhata

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.techquantum.tqdkhata.ui.screens.AddEditClientScreen
import com.techquantum.tqdkhata.ui.screens.ClientDetailScreen
import com.techquantum.tqdkhata.ui.screens.ClientListScreen
import com.techquantum.tqdkhata.ui.screens.RemindersScreen
import com.techquantum.tqdkhata.ui.theme.TQDKhataTheme
import com.techquantum.tqdkhata.ui.theme.WarmBackground
import com.techquantum.tqdkhata.ui.viewmodel.ClientViewModel
import com.techquantum.tqdkhata.ui.viewmodel.ClientViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TQDKhataTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = WarmBackground
                ) {
                    TQDKhataApp()
                }
            }
        }
    }
}

@Composable
fun TQDKhataApp() {
    val context = LocalContext.current
    val viewModel: ClientViewModel = viewModel(
        factory = ClientViewModelFactory(context)
    )
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "clients"
    ) {
        composable("clients") {
            ClientListScreen(
                viewModel = viewModel,
                onNavigateToAddClient = { navController.navigate("client_form") },
                onNavigateToClientDetail = { id -> navController.navigate("client_detail/$id") },
                onNavigateToReminders = { navController.navigate("reminders") }
            )
        }

        composable(
            route = "client_form?clientId={clientId}",
            arguments = listOf(
                navArgument("clientId") {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ) { backStackEntry ->
            val clientId = backStackEntry.arguments?.getLong("clientId") ?: -1L
            AddEditClientScreen(
                clientId = if (clientId > 0L) clientId else null,
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = "client_detail/{clientId}",
            arguments = listOf(navArgument("clientId") { type = NavType.LongType })
        ) { backStackEntry ->
            val clientId = backStackEntry.arguments?.getLong("clientId") ?: 0L
            ClientDetailScreen(
                clientId = clientId,
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToEdit = { id -> navController.navigate("client_form?clientId=$id") }
            )
        }

        composable("reminders") {
            RemindersScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToClientDetail = { id -> navController.navigate("client_detail/$id") }
            )
        }
    }
}
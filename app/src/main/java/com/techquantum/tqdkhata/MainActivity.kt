package com.techquantum.tqdkhata

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
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
import com.techquantum.tqdkhata.util.ReminderAlarmScheduler

class MainActivity : ComponentActivity() {

    private val pendingClientId = mutableStateOf<Long?>(null)

    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { _ ->
        // Permission result handled
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Create notification channel for reminder alerts
        ReminderAlarmScheduler.createNotificationChannel(this)

        // Request POST_NOTIFICATIONS permission on Android 13+ (API 33+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        handleIntent(intent)

        setContent {
            TQDKhataTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = WarmBackground
                ) {
                    TQDKhataApp(
                        pendingClientId = pendingClientId.value,
                        onClearPendingClient = { pendingClientId.value = null }
                    )
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {
        val clientId = intent?.getLongExtra("clientId", -1L) ?: -1L
        if (clientId > 0L) {
            pendingClientId.value = clientId
        }
    }
}

@Composable
fun TQDKhataApp(
    pendingClientId: Long? = null,
    onClearPendingClient: () -> Unit = {}
) {
    val context = LocalContext.current
    val viewModel: ClientViewModel = viewModel(
        factory = ClientViewModelFactory(context)
    )
    val navController = rememberNavController()

    // Deep link navigation when notification is tapped
    LaunchedEffect(pendingClientId) {
        if (pendingClientId != null && pendingClientId > 0L) {
            navController.navigate("client_detail/$pendingClientId")
            onClearPendingClient()
        }
    }

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
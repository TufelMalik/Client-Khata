package com.techquantum.tqdkhata.app

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.techquantum.tqdkhata.modules.clients.viewmodel.ClientViewModel
import com.techquantum.tqdkhata.modules.clients.viewmodel.ClientViewModelFactory
import com.techquantum.tqdkhata.navigation.AppNavHost
import com.techquantum.tqdkhata.theme.TQDKhataTheme
import com.techquantum.tqdkhata.theme.WarmBackground
import com.techquantum.tqdkhata.utils.helpers.ReminderAlarmScheduler

class MainActivity : ComponentActivity() {

    private val rootViewModel: RootViewModel by viewModels()

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
                    val pendingClientId by rootViewModel.pendingClientId.collectAsState()
                    TQDKhataApp(
                        pendingClientId = pendingClientId,
                        onClearPendingClient = { rootViewModel.clearPendingClientId() }
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
            rootViewModel.setPendingClientId(clientId)
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

    AppNavHost(
        navController = navController,
        viewModel = viewModel,
        pendingClientId = pendingClientId,
        onClearPendingClient = onClearPendingClient
    )
}

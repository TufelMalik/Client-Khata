package com.techquantum.tqdkhata.modules.clients.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.EventNote
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FileOpen
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.techquantum.tqdkhata.R
import com.techquantum.tqdkhata.components.ExportOptionsDialog
import com.techquantum.tqdkhata.components.ImportConfirmDialog
import com.techquantum.tqdkhata.components.StatSummaryCard
import com.techquantum.tqdkhata.components.StoragePermissionRationaleDialog
import com.techquantum.tqdkhata.model.data.local.ClientEntity
import com.techquantum.tqdkhata.model.enums.ProjectStatus
import com.techquantum.tqdkhata.model.response.BackupData
import com.techquantum.tqdkhata.modules.clients.components.ClientCard
import com.techquantum.tqdkhata.modules.clients.viewmodel.ClientViewModel
import com.techquantum.tqdkhata.theme.BrandBronze
import com.techquantum.tqdkhata.theme.BrandCream
import com.techquantum.tqdkhata.theme.BrandNavy
import com.techquantum.tqdkhata.theme.BrandSage
import com.techquantum.tqdkhata.theme.StatusDeliveredBg
import com.techquantum.tqdkhata.theme.StatusInProgressBg
import com.techquantum.tqdkhata.theme.StatusLeadBg
import com.techquantum.tqdkhata.theme.TextMuted
import com.techquantum.tqdkhata.theme.TextPrimary
import com.techquantum.tqdkhata.theme.WarmBackground
import com.techquantum.tqdkhata.utils.helpers.IntentUtils
import com.techquantum.tqdkhata.utils.helpers.JsonBackupUtils
import com.techquantum.tqdkhata.utils.helpers.StoragePermissionHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientListContent(
    modifier: Modifier = Modifier,
    clients: List<ClientEntity>,
    searchQuery: String = "",
    selectedCity: String? = null,
    selectedStatus: ProjectStatus? = null,
    distinctCities: List<String> = emptyList(),
    totalCount: Int = clients.size,
    leadsCount: Int = clients.count { it.status == ProjectStatus.NEW_LEAD },
    inProgressCount: Int = clients.count { it.status == ProjectStatus.IN_PROGRESS },
    deliveredCount: Int = clients.count { it.status == ProjectStatus.DELIVERED },
    pendingRemindersCount: Int = 0,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    onSearchQueryChange: (String) -> Unit = {},
    onCitySelected: (String?) -> Unit = {},
    onStatusSelected: (ProjectStatus?) -> Unit = {},
    onNavigateToAddClient: () -> Unit = {},
    onNavigateToClientDetail: (Long) -> Unit = {},
    onNavigateToReminders: () -> Unit = {},
    onExportClick: () -> Unit = {},
    onImportClick: () -> Unit = {},
) {
    var cityDropdownExpanded by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = WarmBackground,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToAddClient,
                containerColor = BrandNavy,
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Client",
                    modifier = Modifier.size(26.dp)
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header Top Bar
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = stringResource(R.string.app_name),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = BrandNavy,
                            letterSpacing = (-0.5).sp
                        )
                        Text(
                            text = "Client & Requirement Tracker",
                            fontSize = 12.sp,
                            color = BrandBronze,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Reminders button with badge
                        IconButton(
                            onClick = onNavigateToReminders,
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(BrandCream)
                        ) {
                            BadgedBox(
                                badge = {
                                    if (pendingRemindersCount > 0) {
                                        Badge(
                                            containerColor = BrandBronze,
                                            contentColor = Color.White
                                        ) {
                                            Text(pendingRemindersCount.toString())
                                        }
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.EventNote,
                                    contentDescription = "Reminders",
                                    tint = BrandNavy
                                )
                            }
                        }

                        // Backup & Restore (JSON) options dropdown
                        Box {
                            var menuExpanded by remember { mutableStateOf(false) }

                            IconButton(
                                onClick = { menuExpanded = true },
                                modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(BrandCream)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.MoreVert,
                                    contentDescription = "Data Options",
                                    tint = BrandNavy
                                )
                            }

                            DropdownMenu(
                                expanded = menuExpanded,
                                onDismissRequest = { menuExpanded = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Export Data (JSON)") },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Default.UploadFile,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    },
                                    onClick = {
                                        menuExpanded = false
                                        onExportClick()
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Import Data (JSON)") },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Default.FileOpen,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    },
                                    onClick = {
                                        menuExpanded = false
                                        onImportClick()
                                    }
                                )
                            }
                        }
                    }
                }
            }

            // Stats Metric Row
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatSummaryCard(
                        count = totalCount,
                        label = "Total Clients",
                        containerColor = BrandSage.copy(alpha = 0.5f),
                        contentColor = BrandNavy
                    )
                    StatSummaryCard(
                        count = leadsCount,
                        label = "New Leads",
                        containerColor = StatusLeadBg,
                        contentColor = BrandNavy
                    )
                    StatSummaryCard(
                        count = inProgressCount,
                        label = "In Progress",
                        containerColor = StatusInProgressBg,
                        contentColor = BrandNavy
                    )
                    StatSummaryCard(
                        count = deliveredCount,
                        label = "Delivered",
                        containerColor = StatusDeliveredBg,
                        contentColor = BrandNavy
                    )
                }
            }

            // Search Bar & City Filter Row
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = onSearchQueryChange,
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp),
                        placeholder = {
                            Text(
                                text = "Search clients or requirements...",
                                fontSize = 13.sp,
                                color = BrandNavy,
                                maxLines = 1,
                                softWrap = false
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                tint = BrandNavy,
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        trailingIcon = {
                            if (searchQuery.isNotBlank()) {
                                IconButton(onClick = { onSearchQueryChange("") }) {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "Clear",
                                        tint = BrandNavy,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        },
                        singleLine = true,
                        maxLines = 1,
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedBorderColor = BrandNavy,
                            unfocusedBorderColor = BrandSage,
                            focusedTextColor = BrandNavy,
                            unfocusedTextColor = BrandNavy,
                            focusedPlaceholderColor = BrandNavy,
                            unfocusedPlaceholderColor = BrandNavy
                        )
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    // City Filter Dropdown
                    Box {
                        IconButton(
                            onClick = { cityDropdownExpanded = true },
                            modifier = Modifier
                                .size(52.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(if (selectedCity != null) BrandNavy else BrandCream)
                        ) {
                            Icon(
                                imageVector = Icons.Default.FilterList,
                                contentDescription = "Filter by City",
                                tint = if (selectedCity != null) Color.White else BrandNavy,
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        DropdownMenu(
                            expanded = cityDropdownExpanded,
                            onDismissRequest = { cityDropdownExpanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("All Cities") },
                                onClick = {
                                    onCitySelected(null)
                                    cityDropdownExpanded = false
                                }
                            )
                            distinctCities.forEach { city ->
                                DropdownMenuItem(
                                    text = { Text(city) },
                                    onClick = {
                                        onCitySelected(city)
                                        cityDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }

            // Show active city filter chip with active clear button
            if (selectedCity != null) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Filtered by City: ",
                            fontSize = 12.sp,
                            color = TextMuted
                        )
                        Text(
                            text = selectedCity,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = BrandNavy
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .clickable { onCitySelected(null) }
                                .background(BrandCream)
                                .padding(horizontal = 8.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Clear ✕",
                                fontSize = 11.sp,
                                color = BrandNavy,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Status Filter Chips
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = selectedStatus == null,
                        onClick = { onStatusSelected(null) },
                        label = { Text("All") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = BrandNavy,
                            selectedLabelColor = Color.White,
                            containerColor = Color.White,
                            labelColor = BrandNavy
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            borderColor = BrandSage,
                            selectedBorderColor = BrandNavy,
                            enabled = true,
                            selected = selectedStatus == null
                        ),
                        shape = RoundedCornerShape(20.dp)
                    )

                    ProjectStatus.entries.forEach { status ->
                        val isSelected = selectedStatus == status
                        FilterChip(
                            selected = isSelected,
                            onClick = { onStatusSelected(status) },
                            label = { Text(status.displayName) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = BrandNavy,
                                selectedLabelColor = Color.White,
                                containerColor = Color.White,
                                labelColor = BrandNavy
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                borderColor = BrandSage,
                                selectedBorderColor = BrandNavy,
                                enabled = true,
                                selected = isSelected
                            ),
                            shape = RoundedCornerShape(20.dp)
                        )
                    }
                }
            }

            // Clients List or Empty State
            if (clients.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 40.dp, bottom = 40.dp, start = 20.dp, end = 20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(72.dp)
                                    .clip(CircleShape)
                                    .background(BrandCream),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.People,
                                    contentDescription = null,
                                    tint = BrandNavy,
                                    modifier = Modifier.size(36.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = if (searchQuery.isNotBlank() || selectedCity != null || selectedStatus != null)
                                    "No clients match your filter"
                                else
                                    "No clients added yet",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextPrimary
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Tap the + button below to add your first client and their requirements.",
                                fontSize = 13.sp,
                                color = TextMuted,
                                textAlign = TextAlign.Center,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }
            } else {
                items(clients, key = { it.id }) { client ->
                    ClientCard(
                        client = client,
                        onClick = { onNavigateToClientDetail(client.id) },
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )
                }
            }
        }
    }
}

enum class PendingBackupAction {
    EXPORT_SAVE,
    EXPORT_SHARE,
    IMPORT
}

@Composable
fun ClientListScreen(
    viewModel: ClientViewModel,
    onNavigateToAddClient: () -> Unit,
    onNavigateToClientDetail: (Long) -> Unit,
    onNavigateToReminders: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val clients by viewModel.clients.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCity by viewModel.selectedCity.collectAsState()
    val selectedStatus by viewModel.selectedStatus.collectAsState()
    val distinctCities by viewModel.distinctCities.collectAsState()

    val totalCount by viewModel.totalCount.collectAsState()
    val leadsCount by viewModel.leadsCount.collectAsState()
    val inProgressCount by viewModel.inProgressCount.collectAsState()
    val deliveredCount by viewModel.deliveredCount.collectAsState()
    val reminders by viewModel.reminders.collectAsState()
    val pendingRemindersCount = reminders.count { !it.isCompleted }

    // Dialog & Action States
    var showExportOptionsDialog by remember { mutableStateOf(false) }
    var showImportConfirmDialog by remember { mutableStateOf(false) }
    var pendingImportData by remember { mutableStateOf<BackupData?>(null) }
    var showPermissionRationaleDialog by remember { mutableStateOf(false) }
    var pendingAction by remember { mutableStateOf<PendingBackupAction?>(null) }

    // Create Document Launcher for JSON Export
    val createDocumentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/json")
    ) { uri: Uri? ->
        if (uri != null) {
            viewModel.exportDataToJson(
                onSuccess = { jsonString ->
                    scope.launch(Dispatchers.IO) {
                        try {
                            context.contentResolver.openOutputStream(uri)?.use { out ->
                                out.write(jsonString.toByteArray(StandardCharsets.UTF_8))
                            }
                            withContext(Dispatchers.Main) {
                                snackbarHostState.showSnackbar("Backup saved successfully")
                            }
                        } catch (e: Exception) {
                            withContext(Dispatchers.Main) {
                                snackbarHostState.showSnackbar(formatShortError("Save failed", e))
                            }
                        }
                    }
                },
                onError = { e ->
                    scope.launch {
                        snackbarHostState.showSnackbar(formatShortError("Export failed", e))
                    }
                }
            )
        }
    }

    // Open Document Launcher for JSON Import
    val openDocumentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        if (uri != null) {
            scope.launch(Dispatchers.IO) {
                try {
                    val content = context.contentResolver.openInputStream(uri)?.use { stream ->
                        stream.bufferedReader().use { it.readText() }
                    }
                    if (content.isNullOrBlank()) {
                        withContext(Dispatchers.Main) {
                            snackbarHostState.showSnackbar("File is empty")
                        }
                        return@launch
                    }
                    val backupData = JsonBackupUtils.parseBackupJson(content)
                    withContext(Dispatchers.Main) {
                        pendingImportData = backupData
                        showImportConfirmDialog = true
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        snackbarHostState.showSnackbar(formatShortError("Invalid file", e))
                    }
                }
            }
        }
    }

    // Runtime Permission Launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionsMap ->
        val allGranted = permissionsMap.values.all { it }
        if (allGranted) {
            when (pendingAction) {
                PendingBackupAction.EXPORT_SAVE -> {
                    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
                    createDocumentLauncher.launch("TQD_Khata_Backup_$timeStamp.json")
                }
                PendingBackupAction.EXPORT_SHARE -> {
                    viewModel.exportDataToJson(
                        onSuccess = { json -> IntentUtils.shareJsonBackup(context, json) },
                        onError = { e ->
                            scope.launch { snackbarHostState.showSnackbar(formatShortError("Export failed", e)) }
                        }
                    )
                }
                PendingBackupAction.IMPORT -> {
                    openDocumentLauncher.launch(arrayOf("application/json", "text/*", "*/*"))
                }
                null -> {}
            }
        } else {
            showPermissionRationaleDialog = true
        }
        pendingAction = null
    }

    val triggerExportSave = {
        if (StoragePermissionHelper.hasStoragePermissions(context)) {
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            createDocumentLauncher.launch("TQD_Khata_Backup_$timeStamp.json")
        } else {
            pendingAction = PendingBackupAction.EXPORT_SAVE
            val req = StoragePermissionHelper.getRequiredPermissions()
            if (req.isNotEmpty()) {
                permissionLauncher.launch(req)
            } else {
                val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
                createDocumentLauncher.launch("TQD_Khata_Backup_$timeStamp.json")
            }
        }
    }

    val triggerExportShare = {
        if (StoragePermissionHelper.hasStoragePermissions(context)) {
            viewModel.exportDataToJson(
                onSuccess = { json -> IntentUtils.shareJsonBackup(context, json) },
                onError = { e ->
                    scope.launch { snackbarHostState.showSnackbar(formatShortError("Export failed", e)) }
                }
            )
        } else {
            pendingAction = PendingBackupAction.EXPORT_SHARE
            val req = StoragePermissionHelper.getRequiredPermissions()
            if (req.isNotEmpty()) {
                permissionLauncher.launch(req)
            } else {
                viewModel.exportDataToJson(
                    onSuccess = { json -> IntentUtils.shareJsonBackup(context, json) },
                    onError = { e ->
                        scope.launch { snackbarHostState.showSnackbar(formatShortError("Export failed", e)) }
                    }
                )
            }
        }
    }

    val triggerImport = {
        if (StoragePermissionHelper.hasStoragePermissions(context)) {
            openDocumentLauncher.launch(arrayOf("application/json", "text/*", "*/*"))
        } else {
            pendingAction = PendingBackupAction.IMPORT
            val req = StoragePermissionHelper.getRequiredPermissions()
            if (req.isNotEmpty()) {
                permissionLauncher.launch(req)
            } else {
                openDocumentLauncher.launch(arrayOf("application/json", "text/*", "*/*"))
            }
        }
    }

    ClientListContent(
        clients = clients,
        searchQuery = searchQuery,
        selectedCity = selectedCity,
        selectedStatus = selectedStatus,
        distinctCities = distinctCities,
        totalCount = totalCount,
        leadsCount = leadsCount,
        inProgressCount = inProgressCount,
        deliveredCount = deliveredCount,
        pendingRemindersCount = pendingRemindersCount,
        snackbarHostState = snackbarHostState,
        onSearchQueryChange = viewModel::onSearchQueryChanged,
        onCitySelected = viewModel::onCitySelected,
        onStatusSelected = viewModel::onStatusSelected,
        onNavigateToAddClient = onNavigateToAddClient,
        onNavigateToClientDetail = onNavigateToClientDetail,
        onNavigateToReminders = onNavigateToReminders,
        onExportClick = { showExportOptionsDialog = true },
        onImportClick = triggerImport,
        modifier = modifier
    )

    // Export Options Dialog
    if (showExportOptionsDialog) {
        ExportOptionsDialog(
            clientCount = totalCount,
            reminderCount = reminders.size,
            onSaveToFile = triggerExportSave,
            onShare = triggerExportShare,
            onDismiss = { showExportOptionsDialog = false }
        )
    }

    // Import Preview & Confirmation Dialog
    pendingImportData?.let { backupData ->
        if (showImportConfirmDialog) {
            ImportConfirmDialog(
                backupData = backupData,
                onConfirm = {
                    showImportConfirmDialog = false
                    viewModel.importBackupData(
                        backupData = backupData,
                        onSuccess = { result ->
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    "Imported ${result.clientsImported} clients, ${result.remindersImported} reminders"
                                )
                            }
                            pendingImportData = null
                        },
                        onError = { err ->
                            scope.launch {
                                snackbarHostState.showSnackbar(formatShortError("Import failed", err))
                            }
                            pendingImportData = null
                        }
                    )
                },
                onDismiss = {
                    showImportConfirmDialog = false
                    pendingImportData = null
                }
            )
        }
    }

    // Storage Permission Rationale Dialog
    if (showPermissionRationaleDialog) {
        StoragePermissionRationaleDialog(
            message = StoragePermissionHelper.getPermissionRationaleMessage(),
            onGrantPermission = {
                showPermissionRationaleDialog = false
                val req = StoragePermissionHelper.getRequiredPermissions()
                if (req.isNotEmpty()) {
                    permissionLauncher.launch(req)
                } else {
                    IntentUtils.openAppSettings(context)
                }
            },
            onDismiss = { showPermissionRationaleDialog = false }
        )
    }
}

private fun formatShortError(prefix: String, error: Throwable?): String {
    val msg = error?.localizedMessage ?: error?.message ?: return prefix
    val reason = when {
        msg.contains("empty", ignoreCase = true) -> "File is empty"
        msg.contains("not a valid JSON", ignoreCase = true) || msg.contains("Unexpected", ignoreCase = true) || msg.contains("JSON", ignoreCase = true) -> "Invalid JSON"
        msg.contains("Permission", ignoreCase = true) || msg.contains("denied", ignoreCase = true) -> "Permission denied"
        msg.contains("space", ignoreCase = true) || msg.contains("full", ignoreCase = true) -> "Storage full"
        msg.contains("not found", ignoreCase = true) -> "File not found"
        msg.contains("format", ignoreCase = true) || msg.contains("structure", ignoreCase = true) -> "Invalid format"
        else -> {
            val stripped = msg.substringAfterLast(":").trim()
            if (stripped.length in 1..25) stripped else "Please try again"
        }
    }
    return "$prefix: $reason"
}

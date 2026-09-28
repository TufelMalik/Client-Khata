package com.techquantum.tqdkhata.ui.screens

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
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.techquantum.tqdkhata.R
import com.techquantum.tqdkhata.data.model.ClientEntity
import com.techquantum.tqdkhata.data.model.ProjectStatus
import com.techquantum.tqdkhata.ui.components.ClientCard
import com.techquantum.tqdkhata.ui.components.StatSummaryCard
import com.techquantum.tqdkhata.ui.theme.BrandBronze
import com.techquantum.tqdkhata.ui.theme.BrandCream
import com.techquantum.tqdkhata.ui.theme.BrandNavy
import com.techquantum.tqdkhata.ui.theme.BrandSage
import com.techquantum.tqdkhata.ui.theme.StatusDeliveredBg
import com.techquantum.tqdkhata.ui.theme.StatusInProgressBg
import com.techquantum.tqdkhata.ui.theme.StatusLeadBg
import com.techquantum.tqdkhata.ui.theme.TextMuted
import com.techquantum.tqdkhata.ui.theme.TextPrimary
import com.techquantum.tqdkhata.ui.theme.WarmBackground
import com.techquantum.tqdkhata.ui.viewmodel.ClientViewModel

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
    onSearchQueryChange: (String) -> Unit = {},
    onCitySelected: (String?) -> Unit = {},
    onStatusSelected: (ProjectStatus?) -> Unit = {},
    onNavigateToAddClient: () -> Unit = {},
    onNavigateToClientDetail: (Long) -> Unit = {},
    onNavigateToReminders: () -> Unit = {},
) {
    var cityDropdownExpanded by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = WarmBackground,
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

@Composable
fun ClientListScreen(
    viewModel: ClientViewModel,
    onNavigateToAddClient: () -> Unit,
    onNavigateToClientDetail: (Long) -> Unit,
    onNavigateToReminders: () -> Unit,
    modifier: Modifier = Modifier
) {
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
        onSearchQueryChange = viewModel::onSearchQueryChanged,
        onCitySelected = viewModel::onCitySelected,
        onStatusSelected = viewModel::onStatusSelected,
        onNavigateToAddClient = onNavigateToAddClient,
        onNavigateToClientDetail = onNavigateToClientDetail,
        onNavigateToReminders = onNavigateToReminders,
        modifier = modifier
    )
}

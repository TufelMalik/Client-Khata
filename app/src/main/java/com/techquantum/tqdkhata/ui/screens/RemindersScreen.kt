package com.techquantum.tqdkhata.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.EventNote
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.techquantum.tqdkhata.data.model.ReminderWithClient
import com.techquantum.tqdkhata.ui.theme.BrandBronze
import com.techquantum.tqdkhata.ui.theme.BrandCream
import com.techquantum.tqdkhata.ui.theme.BrandNavy
import com.techquantum.tqdkhata.ui.theme.BrandSage
import com.techquantum.tqdkhata.ui.theme.TextMuted
import com.techquantum.tqdkhata.ui.theme.TextPrimary
import com.techquantum.tqdkhata.ui.theme.TextSecondary
import com.techquantum.tqdkhata.ui.theme.WarmBackground
import com.techquantum.tqdkhata.ui.theme.WhatsAppGreen
import com.techquantum.tqdkhata.ui.viewmodel.ClientViewModel
import com.techquantum.tqdkhata.util.DateUtils
import com.techquantum.tqdkhata.util.IntentUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RemindersContent(
    modifier: Modifier = Modifier,
    reminders: List<ReminderWithClient>,
    onToggleReminder: (id: Long, completed: Boolean) -> Unit = { _, _ -> },
    onNavigateBack: () -> Unit = {},
    onNavigateToClientDetail: (Long) -> Unit = {}
) {
    val context = LocalContext.current
    var filterPendingOnly by remember { mutableStateOf(true) }

    val filteredList = remember(reminders, filterPendingOnly) {
        if (filterPendingOnly) {
            reminders.filter { !it.isCompleted }
        } else {
            reminders
        }
    }

    Scaffold(
        containerColor = WarmBackground,
        topBar = {
            TopAppBar(
                title = { Text("Follow-ups & Reminders", fontWeight = FontWeight.Bold, color = BrandNavy) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = BrandNavy
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = WarmBackground)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
        ) {
            // Filter row: Pending vs All
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = filterPendingOnly,
                    onClick = { filterPendingOnly = true },
                    label = { Text("Pending (${reminders.count { !it.isCompleted }})") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = BrandNavy,
                        selectedLabelColor = Color.White,
                        containerColor = Color.White,
                        labelColor = BrandNavy
                    ),
                    shape = RoundedCornerShape(16.dp)
                )

                FilterChip(
                    selected = !filterPendingOnly,
                    onClick = { filterPendingOnly = false },
                    label = { Text("All Reminders (${reminders.size})") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = BrandNavy,
                        selectedLabelColor = Color.White,
                        containerColor = Color.White,
                        labelColor = BrandNavy
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (filteredList.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(BrandCream),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.EventNote,
                                contentDescription = null,
                                tint = BrandNavy,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = if (filterPendingOnly) "No pending reminders! You're all caught up." else "No reminders logged yet",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    items(filteredList, key = { it.id }) { item ->
                        ReminderCard(
                            reminder = item,
                            onToggle = { isDone ->
                                onToggleReminder(item.id, isDone)
                            },
                            onClientClick = {
                                onNavigateToClientDetail(item.clientId)
                            },
                            onCallClick = {
                                IntentUtils.openDialer(context, item.clientPhone)
                            },
                            onWhatsAppClick = {
                                IntentUtils.openWhatsApp(context, item.clientPhone)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RemindersScreen(
    viewModel: ClientViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToClientDetail: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val allReminders by viewModel.reminders.collectAsState()

    RemindersContent(
        reminders = allReminders,
        onToggleReminder = viewModel::toggleReminderCompletion,
        onNavigateBack = onNavigateBack,
        onNavigateToClientDetail = onNavigateToClientDetail,
        modifier = modifier
    )
}

@Composable
private fun ReminderCard(
    reminder: ReminderWithClient,
    onToggle: (Boolean) -> Unit,
    onClientClick: () -> Unit,
    onCallClick: () -> Unit,
    onWhatsAppClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (reminder.isCompleted) Color(0xFFF7F7F5) else Color.White
        ),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, BrandSage.copy(alpha = 0.6f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { onToggle(!reminder.isCompleted) },
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = if (reminder.isCompleted) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                        contentDescription = "Toggle status",
                        tint = if (reminder.isCompleted) BrandNavy else TextMuted
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = reminder.title,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (reminder.isCompleted) TextMuted else TextPrimary
                    )

                    if (!reminder.notes.isNullOrBlank()) {
                        Text(
                            text = reminder.notes,
                            fontSize = 12.sp,
                            color = TextSecondary,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Footer with Client Name link and Action buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Client Name click target
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable(onClick = onClientClick)
                        .background(BrandCream.copy(alpha = 0.6f))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = BrandNavy,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = reminder.clientName,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = BrandNavy
                    )
                }

                // Do Time + Call & WhatsApp Quick Buttons
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    if (reminder.reminderTimestamp != null && reminder.reminderTimestamp > 0L) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Schedule,
                                contentDescription = null,
                                tint = BrandBronze,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = DateUtils.formatRelativeTime(reminder.reminderTimestamp),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = BrandBronze
                            )
                        }
                    }

                    IconButton(
                        onClick = onCallClick,
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape),
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = BrandNavy,
                            contentColor = Color.White
                        )
                    ) {
                        Icon(imageVector = Icons.Default.Call, contentDescription = "Call", modifier = Modifier.size(15.dp))
                    }

                    IconButton(
                        onClick = onWhatsAppClick,
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape),
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = WhatsAppGreen,
                            contentColor = Color.White
                        )
                    ) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.Chat, contentDescription = "WhatsApp", modifier = Modifier.size(15.dp))
                    }
                }
            }
        }
    }
}

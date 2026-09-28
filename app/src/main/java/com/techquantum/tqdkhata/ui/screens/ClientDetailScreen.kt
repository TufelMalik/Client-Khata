package com.techquantum.tqdkhata.ui.screens

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.techquantum.tqdkhata.data.model.ClientEntity
import com.techquantum.tqdkhata.data.model.ProjectStatus
import com.techquantum.tqdkhata.data.model.ReminderEntity
import com.techquantum.tqdkhata.ui.components.PriorityBadge
import com.techquantum.tqdkhata.ui.components.StatusBadge
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
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientDetailContent(
    modifier: Modifier = Modifier,
    client: ClientEntity,
    reminders: List<ReminderEntity> = emptyList(),
    onNavigateBack: () -> Unit = {},
    onNavigateToEdit: (Long) -> Unit = {},
    onDeleteClient: () -> Unit = {},
    onUpdateStatus: (ProjectStatus) -> Unit = {},
    onSaveReminder: (ReminderEntity) -> Unit = {},
    onToggleReminder: (reminderId: Long, isCompleted: Boolean) -> Unit = { _, _ -> },
    onDeleteReminder: (ReminderEntity) -> Unit = {}
) {
    val context = LocalContext.current
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }
    var showStatusDialog by remember { mutableStateOf(false) }
    var showAddReminderDialog by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = WarmBackground,
        topBar = {
            TopAppBar(
                title = { Text("Client Details", fontWeight = FontWeight.Bold, color = BrandNavy) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = BrandNavy
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { IntentUtils.shareClientSummary(context, client) }) {
                        Icon(imageVector = Icons.Default.Share, contentDescription = "Share", tint = BrandNavy)
                    }
                    IconButton(onClick = { onNavigateToEdit(client.id) }) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit", tint = BrandNavy)
                    }
                    IconButton(onClick = { showDeleteConfirmDialog = true }) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = WarmBackground)
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header Profile Card
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(18.dp),
                    border = BorderStroke(1.dp, BrandSage.copy(alpha = 0.6f))
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = client.name,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = BrandNavy
                                )
                                if (!client.businessName.isNullOrBlank()) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(top = 4.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Business,
                                            contentDescription = null,
                                            tint = TextSecondary,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = client.businessName,
                                            fontSize = 14.sp,
                                            color = TextSecondary
                                        )
                                    }
                                }
                            }

                            PriorityBadge(priority = client.priority)
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Status Row with Quick Switcher Button
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable { showStatusDialog = true }
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                StatusBadge(status = client.status)
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(
                                    imageVector = Icons.Default.SwapHoriz,
                                    contentDescription = "Change Status",
                                    tint = BrandBronze,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "Change",
                                    fontSize = 11.sp,
                                    color = BrandBronze,
                                    fontWeight = FontWeight.Medium
                                )
                            }

                            if (!client.city.isNullOrBlank()) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.LocationOn,
                                        contentDescription = null,
                                        tint = BrandNavy,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = client.city,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = BrandNavy
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        // Action Buttons: Call & WhatsApp
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = { IntentUtils.openDialer(context, client.phone) },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(44.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = BrandNavy,
                                    contentColor = Color.White
                                )
                            ) {
                                Icon(imageVector = Icons.Default.Call, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Call", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            }

                            Button(
                                onClick = {
                                    IntentUtils.openWhatsApp(context, client.phone)
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(44.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = WhatsAppGreen,
                                    contentColor = Color.White
                                )
                            ) {
                                Icon(imageVector = Icons.AutoMirrored.Filled.Chat, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("WhatsApp", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            }
                        }
                    }
                }
            }

            // Requirements Section
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(18.dp),
                    border = BorderStroke(1.dp, BrandSage.copy(alpha = 0.6f))
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = "Requirements & Project Scope",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = BrandNavy
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFFFCFAF7))
                                .padding(12.dp)
                        ) {
                            Text(
                                text = client.requirements.ifBlank { "No detailed requirements added yet." },
                                fontSize = 14.sp,
                                color = TextPrimary,
                                lineHeight = 20.sp
                            )
                        }

                        if (!client.budget.isNullOrBlank()) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Payments,
                                    contentDescription = null,
                                    tint = BrandBronze,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Estimated Budget: ${client.budget}",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = BrandNavy
                                )
                            }
                        }
                    }
                }
            }

            // Contact & Address Details Section
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(18.dp),
                    border = BorderStroke(1.dp, BrandSage.copy(alpha = 0.6f))
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = "Contact & Location Info",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = BrandNavy
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        ContactDetailRow(
                            icon = Icons.Default.Call,
                            label = "Primary Phone",
                            value = client.phone,
                            onClick = { IntentUtils.openDialer(context, client.phone) }
                        )

                        if (!client.altPhone.isNullOrBlank()) {
                            Spacer(modifier = Modifier.height(10.dp))
                            ContactDetailRow(
                                icon = Icons.Default.Call,
                                label = "Alternate Phone",
                                value = client.altPhone,
                                onClick = { IntentUtils.openDialer(context, client.altPhone) }
                            )
                        }

                        if (!client.email.isNullOrBlank()) {
                            Spacer(modifier = Modifier.height(10.dp))
                            ContactDetailRow(
                                icon = Icons.Default.Email,
                                label = "Email",
                                value = client.email,
                                onClick = { IntentUtils.openEmail(context, client.email) }
                            )
                        }

                        if (!client.address.isNullOrBlank()) {
                            Spacer(modifier = Modifier.height(10.dp))
                            ContactDetailRow(
                                icon = Icons.Default.Home,
                                label = "Address",
                                value = client.address
                            )
                        }
                    }
                }
            }

            // Follow-up Notes & Reminders Section Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Follow-ups & Reminders",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = BrandNavy
                        )
                        Text(
                            text = "${reminders.size} notes logged",
                            fontSize = 12.sp,
                            color = TextMuted
                        )
                    }

                    Button(
                        onClick = { showAddReminderDialog = true },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BrandCream,
                            contentColor = BrandNavy
                        )
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("+ Add Follow-up", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Reminders List
            if (reminders.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color.White)
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.NotificationsActive,
                                contentDescription = null,
                                tint = BrandBronze.copy(alpha = 0.6f),
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "No follow-up notes or reminders yet",
                                fontSize = 13.sp,
                                color = TextMuted
                            )
                        }
                    }
                }
            } else {
                items(reminders, key = { it.id }) { reminder ->
                    ReminderItemCard(
                        reminder = reminder,
                        onToggle = { isDone ->
                            onToggleReminder(reminder.id, isDone)
                        },
                        onDelete = {
                            onDeleteReminder(reminder)
                        }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    // Delete Confirmation Dialog
    // Status Dialog
    if (showStatusDialog) {
        CustomStatusDialog(
            currentStatus = client.status,
            onDismiss = { showStatusDialog = false },
            onStatusSelected = { newStatus ->
                onUpdateStatus(newStatus)
                showStatusDialog = false
            }
        )
    }

    // Delete Confirmation Dialog
    if (showDeleteConfirmDialog) {
        CustomDeleteDialog(
            clientName = client.name,
            onDismiss = { showDeleteConfirmDialog = false },
            onConfirmDelete = {
                showDeleteConfirmDialog = false
                onDeleteClient()
            }
        )
    }

    // Add Reminder Dialog
    if (showAddReminderDialog) {
        CustomAddReminderDialog(
            clientId = client.id,
            clientName = client.name,
            onDismiss = { showAddReminderDialog = false },
            onSave = { reminder ->
                showAddReminderDialog = false
                onSaveReminder(reminder)
            }
        )
    }
}

@Composable
fun ClientDetailScreen(
    clientId: Long,
    viewModel: ClientViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToEdit: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val client by viewModel.getClient(clientId).collectAsState(initial = null)
    val reminders by viewModel.getRemindersForClient(clientId).collectAsState(initial = emptyList())

    if (client == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Client not found", color = TextMuted)
        }
        return
    }

    val currentClient = client!!

    ClientDetailContent(
        client = currentClient,
        reminders = reminders,
        onNavigateBack = onNavigateBack,
        onNavigateToEdit = onNavigateToEdit,
        onDeleteClient = {
            viewModel.deleteClient(currentClient) {
                Toast.makeText(context, "Client deleted", Toast.LENGTH_SHORT).show()
                onNavigateBack()
            }
        },
        onUpdateStatus = { newStatus ->
            viewModel.updateClientStatus(currentClient, newStatus)
            Toast.makeText(context, "Status updated to ${newStatus.displayName}", Toast.LENGTH_SHORT).show()
        },
        onSaveReminder = { reminder ->
            viewModel.saveReminder(reminder) {
                Toast.makeText(context, "Follow-up added", Toast.LENGTH_SHORT).show()
            }
        },
        onToggleReminder = { reminderId, isCompleted ->
            viewModel.toggleReminderCompletion(reminderId, isCompleted)
        },
        onDeleteReminder = { reminder ->
            viewModel.deleteReminder(reminder)
        },
        modifier = modifier
    )
}

@Composable
private fun ContactDetailRow(
    icon: ImageVector,
    label: String,
    value: String,
    onClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .clip(CircleShape)
                .background(BrandCream),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = BrandNavy,
                modifier = Modifier.size(16.dp)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(text = label, fontSize = 11.sp, color = TextMuted)
            Text(text = value, fontSize = 14.sp, color = TextPrimary, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
private fun ReminderItemCard(
    reminder: ReminderEntity,
    onToggle: (Boolean) -> Unit,
    onDelete: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (reminder.isCompleted) Color(0xFFF5F5F3) else Color.White
        ),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, BrandSage.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { onToggle(!reminder.isCompleted) },
                modifier = Modifier.size(28.dp)
            ) {
                Icon(
                    imageVector = if (reminder.isCompleted) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                    contentDescription = "Toggle Status",
                    tint = if (reminder.isCompleted) BrandNavy else TextMuted
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = reminder.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
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

                if (reminder.reminderTimestamp != null && reminder.reminderTimestamp > 0L) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = null,
                            tint = BrandBronze,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = DateUtils.formatRelativeTime(reminder.reminderTimestamp),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = BrandBronze
                        )
                    }
                }
            }

            IconButton(onClick = onDelete, modifier = Modifier.size(28.dp)) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = TextMuted,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
private fun CustomStatusDialog(
    currentStatus: ProjectStatus,
    onDismiss: () -> Unit,
    onStatusSelected: (ProjectStatus) -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, BrandSage.copy(alpha = 0.5f))
        ) {
            Column(
                modifier = Modifier.padding(22.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(BrandCream),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.SwapHoriz,
                        contentDescription = null,
                        tint = BrandNavy,
                        modifier = Modifier.size(26.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Update Status",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = BrandNavy
                )
                Text(
                    text = "Select current progress stage",
                    fontSize = 12.sp,
                    color = TextSecondary,
                    modifier = Modifier.padding(top = 2.dp)
                )

                Spacer(modifier = Modifier.height(18.dp))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ProjectStatus.entries.forEach { status ->
                        val isSelected = status == currentStatus
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) BrandCream.copy(alpha = 0.45f) else Color.Transparent)
                                .border(
                                    width = if (isSelected) 1.5.dp else 1.dp,
                                    color = if (isSelected) BrandNavy else BrandSage.copy(alpha = 0.5f),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .clickable { onStatusSelected(status) }
                                .padding(horizontal = 14.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            StatusBadge(status = status)
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Selected",
                                    tint = BrandNavy,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, BrandSage)
                ) {
                    Text("Cancel", color = BrandNavy, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

@Composable
private fun CustomDeleteDialog(
    clientName: String,
    onDismiss: () -> Unit,
    onConfirmDelete: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, BrandSage.copy(alpha = 0.5f))
        ) {
            Column(
                modifier = Modifier.padding(22.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFEE2E2)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteOutline,
                        contentDescription = null,
                        tint = Color(0xFFDC2626),
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Delete Client?",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = BrandNavy
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Are you sure you want to delete \"$clientName\"? All associated contact info, requirement notes, and reminders will be permanently removed.",
                    fontSize = 13.sp,
                    color = TextSecondary,
                    textAlign = TextAlign.Center,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, BrandSage)
                    ) {
                        Text("Cancel", color = BrandNavy, fontWeight = FontWeight.Medium)
                    }

                    Button(
                        onClick = onConfirmDelete,
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626))
                    ) {
                        Text("Delete", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun CustomAddReminderDialog(
    clientId: Long,
    clientName: String,
    onDismiss: () -> Unit,
    onSave: (ReminderEntity) -> Unit
) {
    val context = LocalContext.current
    var title by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var selectedTimestamp by remember { mutableStateOf<Long?>(null) }

    val calendar = remember { Calendar.getInstance() }

    val showDateTimePicker = {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                TimePickerDialog(
                    context,
                    { _, hourOfDay, minute ->
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        calendar.set(Calendar.MINUTE, minute)
                        calendar.set(Calendar.SECOND, 0)
                        calendar.set(Calendar.MILLISECOND, 0)
                        selectedTimestamp = calendar.timeInMillis
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    false
                ).show()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, BrandSage.copy(alpha = 0.5f))
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(BrandCream),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.NotificationsActive,
                        contentDescription = null,
                        tint = BrandNavy,
                        modifier = Modifier.size(26.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Add Follow-up Reminder",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = BrandNavy
                )
                Text(
                    text = "For $clientName",
                    fontSize = 12.sp,
                    color = TextSecondary,
                    modifier = Modifier.padding(top = 2.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Topic / Purpose *", color = BrandNavy) },
                    placeholder = { Text("e.g. Call for quotation approval", color = BrandNavy.copy(alpha = 0.5f)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BrandNavy,
                        unfocusedBorderColor = BrandSage,
                        focusedTextColor = BrandNavy,
                        unfocusedTextColor = BrandNavy,
                        focusedLabelColor = BrandNavy,
                        unfocusedLabelColor = BrandNavy,
                        focusedPlaceholderColor = BrandNavy.copy(alpha = 0.5f),
                        unfocusedPlaceholderColor = BrandNavy.copy(alpha = 0.5f)
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Notes (Optional)", color = BrandNavy) },
                    placeholder = { Text("Add any extra notes or requirements", color = BrandNavy.copy(alpha = 0.5f)) },
                    minLines = 2,
                    maxLines = 3,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BrandNavy,
                        unfocusedBorderColor = BrandSage,
                        focusedTextColor = BrandNavy,
                        unfocusedTextColor = BrandNavy,
                        focusedLabelColor = BrandNavy,
                        unfocusedLabelColor = BrandNavy,
                        focusedPlaceholderColor = BrandNavy.copy(alpha = 0.5f),
                        unfocusedPlaceholderColor = BrandNavy.copy(alpha = 0.5f)
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Quick Scheduling Section
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Schedule Time",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = BrandNavy
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        // Today 5 PM
                        FilterChip(
                            selected = false,
                            onClick = {
                                val cal = Calendar.getInstance().apply {
                                    set(Calendar.HOUR_OF_DAY, 17)
                                    set(Calendar.MINUTE, 0)
                                    set(Calendar.SECOND, 0)
                                    set(Calendar.MILLISECOND, 0)
                                }
                                selectedTimestamp = cal.timeInMillis
                            },
                            label = { Text("Today 5 PM", fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                containerColor = BrandCream.copy(alpha = 0.5f),
                                labelColor = BrandNavy
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = false,
                                borderColor = BrandSage
                            ),
                            shape = RoundedCornerShape(8.dp)
                        )

                        // Tomorrow 11 AM
                        FilterChip(
                            selected = false,
                            onClick = {
                                val cal = Calendar.getInstance().apply {
                                    add(Calendar.DAY_OF_YEAR, 1)
                                    set(Calendar.HOUR_OF_DAY, 11)
                                    set(Calendar.MINUTE, 0)
                                    set(Calendar.SECOND, 0)
                                    set(Calendar.MILLISECOND, 0)
                                }
                                selectedTimestamp = cal.timeInMillis
                            },
                            label = { Text("Tomorrow 11 AM", fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                containerColor = BrandCream.copy(alpha = 0.5f),
                                labelColor = BrandNavy
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = false,
                                borderColor = BrandSage
                            ),
                            shape = RoundedCornerShape(8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedButton(
                        onClick = showDateTimePicker,
                        modifier = Modifier.fillMaxWidth().height(38.dp),
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, BrandSage)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Event,
                            contentDescription = null,
                            tint = BrandNavy,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (selectedTimestamp != null)
                                DateUtils.formatDateTime(selectedTimestamp)
                            else
                                "Pick Custom Date & Time",
                            fontSize = 12.sp,
                            color = BrandNavy,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    if (selectedTimestamp != null) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Clear schedule",
                                fontSize = 11.sp,
                                color = TextMuted,
                                modifier = Modifier
                                    .clickable { selectedTimestamp = null }
                                    .padding(4.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, BrandSage)
                    ) {
                        Text("Cancel", color = BrandNavy, fontWeight = FontWeight.Medium)
                    }

                    Button(
                        onClick = {
                            if (title.isBlank()) {
                                Toast.makeText(context, "Please enter a topic", Toast.LENGTH_SHORT).show()
                                return@Button
                            }
                            val reminder = ReminderEntity(
                                clientId = clientId,
                                title = title.trim(),
                                notes = notes.trim().ifBlank { null },
                                reminderTimestamp = selectedTimestamp
                            )
                            onSave(reminder)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = BrandNavy)
                    ) {
                        Text("Save", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

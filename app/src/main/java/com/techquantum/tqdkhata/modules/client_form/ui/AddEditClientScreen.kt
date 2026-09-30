package com.techquantum.tqdkhata.modules.client_form.ui

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.Manifest
import java.util.Calendar
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import com.techquantum.tqdkhata.model.data.local.ReminderEntity
import com.techquantum.tqdkhata.utils.helpers.DateUtils
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.produceState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import com.techquantum.tqdkhata.utils.helpers.MediaUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.techquantum.tqdkhata.model.data.local.ClientEntity
import com.techquantum.tqdkhata.model.enums.Priority
import com.techquantum.tqdkhata.model.enums.ProjectStatus
import com.techquantum.tqdkhata.theme.BrandBronze
import com.techquantum.tqdkhata.theme.BrandCream
import com.techquantum.tqdkhata.theme.BrandNavy
import com.techquantum.tqdkhata.theme.BrandSage
import com.techquantum.tqdkhata.theme.TextMuted
import com.techquantum.tqdkhata.theme.TextPrimary
import com.techquantum.tqdkhata.theme.TextSecondary
import com.techquantum.tqdkhata.theme.WarmBackground
import com.techquantum.tqdkhata.modules.clients.viewmodel.ClientViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditClientContent(
    modifier: Modifier = Modifier,
    initialClient: ClientEntity? = null,
    onSaveClient: (ClientEntity, ReminderEntity?) -> Unit = { _, _ -> },
    onNavigateBack: () -> Unit = {}
) {
    val context = LocalContext.current
    val isEditMode = initialClient != null && initialClient.id > 0L

    var name by remember(initialClient) { mutableStateOf(initialClient?.name ?: "") }
    var phone by remember(initialClient) { mutableStateOf(initialClient?.phone ?: "") }
    var altPhone by remember(initialClient) { mutableStateOf(initialClient?.altPhone ?: "") }
    var email by remember(initialClient) { mutableStateOf(initialClient?.email ?: "") }
    var businessName by remember(initialClient) { mutableStateOf(initialClient?.businessName ?: "") }
    var businessType by remember(initialClient) { mutableStateOf(initialClient?.businessType ?: "") }
    var requirements by remember(initialClient) { mutableStateOf(initialClient?.requirements ?: "") }
    var budget by remember(initialClient) { mutableStateOf(initialClient?.budget ?: "") }
    var city by remember(initialClient) { mutableStateOf(initialClient?.city ?: "Bharuch") }
    var address by remember(initialClient) { mutableStateOf(initialClient?.address ?: "") }
    var status by remember(initialClient) { mutableStateOf(initialClient?.status ?: ProjectStatus.NEW_LEAD) }
    var priority by remember(initialClient) { mutableStateOf(initialClient?.priority ?: Priority.MEDIUM) }

    var nameError by remember { mutableStateOf(false) }
    var phoneError by remember { mutableStateOf(false) }

    var shopImagePath by remember(initialClient) { mutableStateOf(initialClient?.shopImagePath) }
    var showPhotoSourceDialog by remember { mutableStateOf(false) }
    var pendingPhotoPath by rememberSaveable { mutableStateOf<String?>(null) }

    // Follow-up Reminder States
    var enableReminder by remember { mutableStateOf(false) }
    var reminderTitle by remember { mutableStateOf("") }
    var reminderNotes by remember { mutableStateOf("") }
    var reminderTimestamp by remember { mutableStateOf<Long?>(null) }
    var reminderTitleError by remember { mutableStateOf(false) }

    val reminderCalendar = remember { Calendar.getInstance() }
    val showReminderDateTimePicker = {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                reminderCalendar.set(Calendar.YEAR, year)
                reminderCalendar.set(Calendar.MONTH, month)
                reminderCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                TimePickerDialog(
                    context,
                    { _, hourOfDay, minute ->
                        reminderCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        reminderCalendar.set(Calendar.MINUTE, minute)
                        reminderCalendar.set(Calendar.SECOND, 0)
                        reminderCalendar.set(Calendar.MILLISECOND, 0)
                        reminderTimestamp = reminderCalendar.timeInMillis
                    },
                    reminderCalendar.get(Calendar.HOUR_OF_DAY),
                    reminderCalendar.get(Calendar.MINUTE),
                    false
                ).show()
            },
            reminderCalendar.get(Calendar.YEAR),
            reminderCalendar.get(Calendar.MONTH),
            reminderCalendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    val clientIdForMedia = if (initialClient != null && initialClient.id > 0L) initialClient.id else 0L

    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { _ ->
        val savedPath = pendingPhotoPath ?: MediaUtils.getPendingPhotoPath(context, clientIdForMedia)
        val photoFile = savedPath?.let { File(it) }
        if (photoFile != null && photoFile.exists() && photoFile.length() > 0L) {
            shopImagePath = photoFile.absolutePath
            Toast.makeText(context, "Shop photo captured", Toast.LENGTH_SHORT).show()
        } else {
            photoFile?.delete()
        }
        pendingPhotoPath = null
        MediaUtils.clearPendingPhotoPath(context, clientIdForMedia)
    }

    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            val targetId = if (clientIdForMedia > 0L) clientIdForMedia else System.currentTimeMillis()
            val savedFile = MediaUtils.copyUriToLocalResource(context, uri, targetId, isVideo = false)
            if (savedFile != null && savedFile.exists() && savedFile.length() > 0L) {
                shopImagePath = savedFile.absolutePath
                Toast.makeText(context, "Shop photo attached", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Failed to load photo", Toast.LENGTH_SHORT).show()
            }
        }
    }

    var cameraActionPending by remember { mutableStateOf<(() -> Unit)?>(null) }
    val requestCameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            cameraActionPending?.invoke()
        } else {
            Toast.makeText(context, "Camera permission is required to capture photos", Toast.LENGTH_LONG).show()
        }
        cameraActionPending = null
    }

    val launchWithCameraPermission: (() -> Unit) -> Unit = { action ->
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            action()
        } else {
            cameraActionPending = action
            requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    Scaffold(
        containerColor = WarmBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (isEditMode) "Edit Client" else "Add New Client",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = BrandNavy
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = BrandNavy
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = WarmBackground
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {

            // Section: Shop Photo
            SectionHeader(title = "Shop / Business Photo", subtitle = "Profile picture for client card & details")

            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, BrandSage.copy(alpha = 0.5f))
            ) {
                if (shopImagePath != null) {
                    val currentPath = shopImagePath!!
                    val bitmap = produceState<Bitmap?>(initialValue = null, currentPath) {
                        value = withContext(Dispatchers.IO) {
                            val file = File(currentPath)
                            if (file.exists()) MediaUtils.loadThumbnail(currentPath) else null
                        }
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFFCFAF7))
                        ) {
                            if (bitmap.value != null) {
                                Image(
                                    bitmap = bitmap.value!!.asImageBitmap(),
                                    contentDescription = "Shop Photo Preview",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedButton(
                                onClick = { showPhotoSourceDialog = true },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(42.dp),
                                shape = RoundedCornerShape(10.dp),
                                border = BorderStroke(1.dp, BrandNavy)
                            ) {
                                Icon(imageVector = Icons.Default.CameraAlt, contentDescription = null, tint = BrandNavy, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Change", color = BrandNavy, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                            }

                            OutlinedButton(
                                onClick = { shopImagePath = null },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(42.dp),
                                shape = RoundedCornerShape(10.dp),
                                border = BorderStroke(1.dp, Color.Red.copy(alpha = 0.5f))
                            ) {
                                Icon(imageVector = Icons.Default.Delete, contentDescription = null, tint = Color.Red, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Remove", color = Color.Red, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showPhotoSourceDialog = true }
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .clip(CircleShape)
                                    .background(BrandCream),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Storefront,
                                    contentDescription = null,
                                    tint = BrandNavy,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "Add Shop / Business Photo",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = BrandNavy
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Tap to capture from camera or choose from gallery",
                                fontSize = 12.sp,
                                color = TextMuted,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Section: Basic Info
            SectionHeader(title = "Client Contact Details", subtitle = "Primary communication information")

            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, BrandSage.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    InputField(
                        value = name,
                        onValueChange = {
                            name = it
                            if (nameError && it.isNotBlank()) nameError = false
                        },
                        label = "Client Name *",
                        icon = Icons.Default.Person,
                        isError = nameError,
                        errorMessage = "Client name is required"
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    InputField(
                        value = phone,
                        onValueChange = {
                            phone = it
                            if (phoneError && it.isNotBlank()) phoneError = false
                        },
                        label = "Phone Number * (Call & WhatsApp)",
                        icon = Icons.Default.Phone,
                        keyboardType = KeyboardType.Phone,
                        isError = phoneError,
                        errorMessage = "Phone number is required"
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    InputField(
                        value = altPhone,
                        onValueChange = { altPhone = it },
                        label = "Alternate Phone (Optional)",
                        icon = Icons.Default.Phone,
                        keyboardType = KeyboardType.Phone
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    InputField(
                        value = email,
                        onValueChange = { email = it },
                        label = "Email Address (Optional)",
                        icon = Icons.Default.Email,
                        keyboardType = KeyboardType.Email
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    InputField(
                        value = businessName,
                        onValueChange = { businessName = it },
                        label = "Business / Company Name (Optional)",
                        icon = Icons.Default.Business
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    InputField(
                        value = businessType,
                        onValueChange = { businessType = it },
                        label = "Business Type (Optional)",
                        icon = Icons.Default.Category
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    val businessTypeSuggestions = listOf(
                        "Retail", "Wholesale", "Services", "Restaurant",
                        "IT / Software", "Construction", "Manufacturing", "Pharma"
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        businessTypeSuggestions.forEach { suggestion ->
                            val isSelected = businessType.equals(suggestion, ignoreCase = true)
                            FilterChip(
                                selected = isSelected,
                                onClick = {
                                    businessType = if (isSelected) "" else suggestion
                                },
                                label = { Text(suggestion, fontSize = 11.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = BrandNavy,
                                    selectedLabelColor = Color.White,
                                    containerColor = BrandCream.copy(alpha = 0.5f),
                                    labelColor = BrandNavy
                                ),
                                shape = RoundedCornerShape(16.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Section: Location Info
            SectionHeader(title = "Location", subtitle = "City & Site Address")

            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, BrandSage.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    InputField(
                        value = city,
                        onValueChange = { city = it },
                        label = "City",
                        icon = Icons.Default.LocationCity
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    InputField(
                        value = address,
                        onValueChange = { address = it },
                        label = "Full Address / Landmark (Optional)",
                        icon = Icons.Default.Home
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Section: Project & Requirements
            SectionHeader(title = "Project & Requirements", subtitle = "Deliverables, budget, and project status")

            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, BrandSage.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    InputField(
                        value = requirements,
                        onValueChange = { requirements = it },
                        label = "Client Requirements / Scope of Work",
                        icon = Icons.AutoMirrored.Filled.Notes,
                        singleLine = false,
                        minLines = 3
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    InputField(
                        value = budget,
                        onValueChange = { budget = it },
                        label = "Estimated Budget (e.g. ₹50,000)",
                        icon = Icons.Default.Payments
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Project Status Picker
                    Text(
                        text = "Project Status",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ProjectStatus.entries.forEach { st ->
                            val isSelected = status == st
                            FilterChip(
                                selected = isSelected,
                                onClick = { status = st },
                                label = { Text(st.displayName) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = BrandNavy,
                                    selectedLabelColor = Color.White,
                                    containerColor = BrandCream.copy(alpha = 0.5f),
                                    labelColor = BrandNavy
                                ),
                                shape = RoundedCornerShape(20.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Priority Picker
                    Text(
                        text = "Priority Level",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Priority.entries.forEach { pr ->
                            val isSelected = priority == pr
                            FilterChip(
                                selected = isSelected,
                                onClick = { priority = pr },
                                modifier = Modifier.weight(1f),
                                label = {
                                    Box(
                                        modifier = Modifier.fillMaxWidth(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = pr.label,
                                            fontSize = 13.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                        )
                                    }
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = BrandBronze,
                                    selectedLabelColor = Color.White,
                                    containerColor = BrandSage.copy(alpha = 0.3f),
                                    labelColor = BrandNavy
                                ),
                                shape = RoundedCornerShape(12.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Section: Follow-up Reminder (Optional)
            SectionHeader(title = "Follow-up Reminder (Optional)", subtitle = "Schedule an initial task or reminder")

            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, BrandSage.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(if (enableReminder) BrandNavy else BrandCream),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.NotificationsActive,
                                    contentDescription = null,
                                    tint = if (enableReminder) Color.White else BrandNavy,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Set a follow-up reminder",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = BrandNavy
                                )
                                Text(
                                    text = if (enableReminder) "Active alert scheduled" else "Optional notification alert",
                                    fontSize = 11.sp,
                                    color = TextMuted
                                )
                            }
                        }

                        Switch(
                            checked = enableReminder,
                            onCheckedChange = { enableReminder = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = BrandNavy,
                                uncheckedThumbColor = Color.White,
                                uncheckedTrackColor = BrandSage.copy(alpha = 0.5f)
                            )
                        )
                    }

                    if (enableReminder) {
                        Spacer(modifier = Modifier.height(14.dp))

                        // Quick Reminder Topic Chips
                        Text(
                            text = "Quick Topics",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextSecondary
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            listOf("Follow-up Call", "Send Quotation", "Site Measurement", "Payment Discussion").forEach { topic ->
                                val isSelected = reminderTitle == topic
                                FilterChip(
                                    selected = isSelected,
                                    onClick = {
                                        reminderTitle = topic
                                        if (reminderTitleError) reminderTitleError = false
                                    },
                                    label = { Text(topic, fontSize = 11.sp) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = BrandNavy,
                                        selectedLabelColor = Color.White,
                                        containerColor = BrandCream.copy(alpha = 0.5f),
                                        labelColor = BrandNavy
                                    ),
                                    shape = RoundedCornerShape(8.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        InputField(
                            value = reminderTitle,
                            onValueChange = {
                                reminderTitle = it
                                if (reminderTitleError && it.isNotBlank()) reminderTitleError = false
                            },
                            label = "Reminder Topic / Purpose *",
                            icon = Icons.AutoMirrored.Filled.Notes,
                            isError = reminderTitleError,
                            errorMessage = if (reminderTitleError) "Please enter reminder topic" else null
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        InputField(
                            value = reminderNotes,
                            onValueChange = { reminderNotes = it },
                            label = "Notes (Optional)",
                            icon = Icons.AutoMirrored.Filled.Notes,
                            singleLine = false,
                            minLines = 2
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Date Time Schedule
                        Text(
                            text = "Schedule Alert Time",
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
                                    reminderTimestamp = cal.timeInMillis
                                },
                                label = { Text("Today 5 PM", fontSize = 11.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    containerColor = BrandCream.copy(alpha = 0.5f),
                                    labelColor = BrandNavy
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
                                    reminderTimestamp = cal.timeInMillis
                                },
                                label = { Text("Tomorrow 11 AM", fontSize = 11.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    containerColor = BrandCream.copy(alpha = 0.5f),
                                    labelColor = BrandNavy
                                ),
                                shape = RoundedCornerShape(8.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        OutlinedButton(
                            onClick = showReminderDateTimePicker,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(42.dp),
                            shape = RoundedCornerShape(10.dp),
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
                                text = if (reminderTimestamp != null)
                                    DateUtils.formatDateTime(reminderTimestamp)
                                else
                                    "Pick Custom Date & Time",
                                fontSize = 12.sp,
                                color = BrandNavy,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        if (reminderTimestamp != null) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(BrandCream.copy(alpha = 0.7f))
                                    .padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.NotificationsActive,
                                    contentDescription = null,
                                    tint = BrandBronze,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Alert scheduled for ${DateUtils.formatDateTime(reminderTimestamp)}",
                                    fontSize = 11.sp,
                                    color = BrandNavy,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.weight(1f)
                                )
                                Text(
                                    text = "Clear",
                                    fontSize = 11.sp,
                                    color = TextMuted,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier
                                        .clickable { reminderTimestamp = null }
                                        .padding(4.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Save Button
            Button(
                onClick = {
                    if (name.isBlank()) {
                        nameError = true
                    }
                    if (phone.isBlank()) {
                        phoneError = true
                    }
                    if (name.isBlank() || phone.isBlank()) {
                        Toast.makeText(context, "Please enter client name and phone number", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    if (enableReminder && reminderTitle.isBlank()) {
                        reminderTitleError = true
                        Toast.makeText(context, "Please enter reminder topic or turn off reminder", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    val clientToSave = ClientEntity(
                        id = initialClient?.id ?: 0L,
                        name = name.trim(),
                        phone = phone.trim(),
                        altPhone = altPhone.trim().ifBlank { null },
                        email = email.trim().ifBlank { null },
                        businessName = businessName.trim().ifBlank { null },
                        businessType = businessType.trim().ifBlank { null },
                        requirements = requirements.trim(),
                        budget = budget.trim().ifBlank { null },
                        status = status,
                        priority = priority,
                        address = address.trim().ifBlank { null },
                        city = city.trim().ifBlank { null },
                        shopImagePath = shopImagePath
                    )

                    val reminderToSave = if (enableReminder && reminderTitle.isNotBlank()) {
                        ReminderEntity(
                            clientId = initialClient?.id ?: 0L,
                            title = reminderTitle.trim(),
                            notes = reminderNotes.trim().ifBlank { null },
                            reminderTimestamp = reminderTimestamp
                        )
                    } else null

                    onSaveClient(clientToSave, reminderToSave)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BrandNavy,
                    contentColor = Color.White
                )
            ) {
                Icon(imageVector = Icons.Default.Check, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isEditMode) "Update Client Details" else "Save Client to Khata",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }

    if (showPhotoSourceDialog) {
        Dialog(onDismissRequest = { showPhotoSourceDialog = false }) {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(
                    modifier = Modifier.padding(22.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(BrandCream),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = null,
                            tint = BrandNavy,
                            modifier = Modifier.size(26.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Shop / Business Photo",
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold,
                        color = BrandNavy
                    )
                    Text(
                        text = "Choose source for shop picture",
                        fontSize = 12.sp,
                        color = TextSecondary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 2.dp)
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(BrandCream.copy(alpha = 0.35f))
                            .border(1.dp, BrandSage.copy(alpha = 0.45f), RoundedCornerShape(14.dp))
                            .clickable {
                                showPhotoSourceDialog = false
                                launchWithCameraPermission {
                                    val file = MediaUtils.createMediaFile(context, clientIdForMedia, isVideo = false)
                                    pendingPhotoPath = file.absolutePath
                                    MediaUtils.setPendingPhotoPath(context, clientIdForMedia, file.absolutePath)
                                    val uri = MediaUtils.getFileUri(context, file)
                                    takePictureLauncher.launch(uri)
                                }
                            }
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(BrandNavy),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(imageVector = Icons.Default.CameraAlt, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text(text = "Take Photo", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = BrandNavy)
                            Text(text = "Capture with camera", fontSize = 12.sp, color = TextMuted)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(BrandCream.copy(alpha = 0.35f))
                            .border(1.dp, BrandSage.copy(alpha = 0.45f), RoundedCornerShape(14.dp))
                            .clickable {
                                showPhotoSourceDialog = false
                                pickImageLauncher.launch("image/*")
                            }
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(BrandBronze),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(imageVector = Icons.Default.PhotoLibrary, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text(text = "Upload Photo", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = BrandNavy)
                            Text(text = "Select from gallery", fontSize = 12.sp, color = TextMuted)
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    OutlinedButton(
                        onClick = { showPhotoSourceDialog = false },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, BrandSage)
                    ) {
                        Text("Cancel", color = BrandNavy, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}

@Composable
fun AddEditClientScreen(
    clientId: Long?,
    viewModel: ClientViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val isEditMode = clientId != null && clientId > 0L

    val existingClient by if (clientId != null && clientId > 0L) {
        viewModel.getClient(clientId).collectAsState(initial = null)
    } else {
        remember { mutableStateOf(null) }
    }

    AddEditClientContent(
        initialClient = existingClient,
        onSaveClient = { clientToSave, reminderToSave ->
            viewModel.saveClient(clientToSave) { savedClientId ->
                if (reminderToSave != null) {
                    viewModel.saveReminder(reminderToSave.copy(clientId = savedClientId))
                }
                Toast.makeText(
                    context,
                    if (isEditMode) "Client updated successfully!" else "Client saved successfully!",
                    Toast.LENGTH_SHORT
                ).show()
                onNavigateBack()
            }
        },
        onNavigateBack = onNavigateBack,
        modifier = modifier
    )
}

@Composable
private fun SectionHeader(title: String, subtitle: String) {
    Column(modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)) {
        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = BrandNavy
        )
        Text(
            text = subtitle,
            fontSize = 11.sp,
            color = TextMuted
        )
    }
}

@Composable
private fun InputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    singleLine: Boolean = true,
    minLines: Int = 1,
    isError: Boolean = false,
    errorMessage: String? = null
) {
    val isMultiLine = minLines > 1 || !singleLine

    Column(modifier = modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = if (isMultiLine) {
                Modifier
                    .fillMaxWidth()
                    .height(130.dp)
            } else {
                Modifier.fillMaxWidth()
            },
            label = {
                Text(
                    text = label,
                    fontSize = 13.sp,
                    color = BrandNavy
                )
            },
            leadingIcon = {
                if (isMultiLine) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(top = 16.dp),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = BrandNavy,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                } else {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = BrandNavy,
                        modifier = Modifier.size(20.dp)
                    )
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            singleLine = singleLine,
            minLines = minLines,
            isError = isError,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = BrandNavy,
                unfocusedBorderColor = BrandSage,
                focusedContainerColor = Color(0xFFFCFAF7),
                unfocusedContainerColor = Color(0xFFFCFAF7),
                focusedTextColor = BrandNavy,
                unfocusedTextColor = BrandNavy,
                focusedLabelColor = BrandNavy,
                unfocusedLabelColor = BrandNavy,
                focusedPlaceholderColor = BrandNavy,
                unfocusedPlaceholderColor = BrandNavy,
                focusedLeadingIconColor = BrandNavy,
                unfocusedLeadingIconColor = BrandNavy
            )
        )
        if (isError && !errorMessage.isNullOrBlank()) {
            Text(
                text = errorMessage,
                color = Color.Red,
                fontSize = 11.sp,
                modifier = Modifier.padding(start = 12.dp, top = 2.dp)
            )
        }
    }
}

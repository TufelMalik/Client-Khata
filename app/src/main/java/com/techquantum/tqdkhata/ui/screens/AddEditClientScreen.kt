package com.techquantum.tqdkhata.ui.screens

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.techquantum.tqdkhata.data.model.ClientEntity
import com.techquantum.tqdkhata.data.model.Priority
import com.techquantum.tqdkhata.data.model.ProjectStatus
import com.techquantum.tqdkhata.ui.theme.BrandBronze
import com.techquantum.tqdkhata.ui.theme.BrandCream
import com.techquantum.tqdkhata.ui.theme.BrandNavy
import com.techquantum.tqdkhata.ui.theme.BrandSage
import com.techquantum.tqdkhata.ui.theme.TextMuted
import com.techquantum.tqdkhata.ui.theme.TextPrimary
import com.techquantum.tqdkhata.ui.theme.WarmBackground
import com.techquantum.tqdkhata.ui.viewmodel.ClientViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditClientContent(
    modifier: Modifier = Modifier,
    initialClient: ClientEntity? = null,
    onSaveClient: (ClientEntity) -> Unit = {},
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
                        city = city.trim().ifBlank { null }
                    )

                    onSaveClient(clientToSave)
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
        onSaveClient = { clientToSave ->
            viewModel.saveClient(clientToSave) {
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

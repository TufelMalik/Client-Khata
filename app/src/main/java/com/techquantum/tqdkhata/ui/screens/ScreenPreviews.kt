package com.techquantum.tqdkhata.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.techquantum.tqdkhata.data.model.ClientEntity
import com.techquantum.tqdkhata.data.model.Priority
import com.techquantum.tqdkhata.data.model.ProjectStatus
import com.techquantum.tqdkhata.data.model.ReminderEntity
import com.techquantum.tqdkhata.data.model.ReminderWithClient
import com.techquantum.tqdkhata.ui.components.ClientCard
import com.techquantum.tqdkhata.ui.components.PriorityBadge
import com.techquantum.tqdkhata.ui.components.StatSummaryCard
import com.techquantum.tqdkhata.ui.components.StatusBadge
import com.techquantum.tqdkhata.ui.theme.BrandCream
import com.techquantum.tqdkhata.ui.theme.BrandNavy
import com.techquantum.tqdkhata.ui.theme.TQDKhataTheme

object DemoData {
    val sampleClient1 = ClientEntity(
        id = 1,
        name = "Rajesh Kumar",
        phone = "9876543210",
        altPhone = "9123456789",
        email = "rajesh.kumar@auratech.com",
        businessName = "Aura Tech Solutions",
        requirements = "Complete Android app development with Room DB, offline sync, and WhatsApp integration for client tracking.",
        budget = "₹50,000",
        status = ProjectStatus.IN_PROGRESS,
        priority = Priority.HIGH,
        city = "Ahmedabad",
        address = "101, Titanium City Center, Satellite, Ahmedabad"
    )

    val sampleClient2 = ClientEntity(
        id = 2,
        name = "Priya Sharma",
        phone = "9823456789",
        altPhone = null,
        email = "priya@designs.in",
        businessName = "Priya Interior Designs",
        requirements = "UI/UX design and mobile application for interior portfolio presentation.",
        budget = "₹75,000",
        status = ProjectStatus.NEW_LEAD,
        priority = Priority.MEDIUM,
        city = "Mumbai",
        address = "B-402, Lotus Business Park, Andheri West, Mumbai"
    )

    val sampleClient3 = ClientEntity(
        id = 3,
        name = "Amit Patel",
        phone = "9712345678",
        altPhone = "9898989898",
        email = "amit@patelelectronics.com",
        businessName = "Patel Electronics",
        requirements = "E-commerce web portal and Android app for retail electronics store with payment gateway.",
        budget = "₹1,20,000",
        status = ProjectStatus.DELIVERED,
        priority = Priority.LOW,
        city = "Surat",
        address = "55, Ring Road, Surat"
    )

    val sampleClient4 = ClientEntity(
        id = 4,
        name = "Sneha Verma",
        phone = "9638527410",
        altPhone = null,
        email = "sneha@vermaconsulting.com",
        businessName = "Verma Business Consulting",
        requirements = "CRM integration and automated follow-up notification module.",
        budget = "₹35,000",
        status = ProjectStatus.QUOTED,
        priority = Priority.HIGH,
        city = "Vadodara",
        address = "12, Alkapuri Arcade, Vadodara"
    )

    val sampleClients = listOf(sampleClient1, sampleClient2, sampleClient3, sampleClient4)

    val sampleReminders = listOf(
        ReminderEntity(
            id = 101,
            clientId = 1,
            title = "Call Rajesh for requirement approval",
            notes = "Discuss offline database sync requirements and budget breakdown",
            reminderTimestamp = System.currentTimeMillis() + 86400000L,
            isCompleted = false
        ),
        ReminderEntity(
            id = 102,
            clientId = 1,
            title = "Send advance payment invoice",
            notes = "Invoice amount: ₹25,000 (50% advance)",
            reminderTimestamp = System.currentTimeMillis() - 3600000L,
            isCompleted = true
        )
    )

    val sampleRemindersWithClient = listOf(
        ReminderWithClient(
            id = 101,
            clientId = 1,
            clientName = "Rajesh Kumar",
            clientPhone = "9876543210",
            title = "Follow up on contract signing",
            notes = "Review contract agreement and obtain digital signature",
            reminderTimestamp = System.currentTimeMillis() + 86400000L,
            isCompleted = false,
            createdAt = System.currentTimeMillis() - 172800000L
        ),
        ReminderWithClient(
            id = 102,
            clientId = 2,
            clientName = "Priya Sharma",
            clientPhone = "9823456789",
            title = "Send initial UI design mockup",
            notes = "Share Figma prototype for interior design portfolio app",
            reminderTimestamp = System.currentTimeMillis() + 172800000L,
            isCompleted = false,
            createdAt = System.currentTimeMillis() - 86400000L
        ),
        ReminderWithClient(
            id = 103,
            clientId = 3,
            clientName = "Amit Patel",
            clientPhone = "9712345678",
            title = "Final project handover call",
            notes = "Deliver source code and admin panel credentials",
            reminderTimestamp = System.currentTimeMillis() - 86400000L,
            isCompleted = true,
            createdAt = System.currentTimeMillis() - 432000000L
        )
    )

    val distinctCities = listOf("Ahmedabad", "Mumbai", "Surat", "Vadodara")
}

@Preview(showBackground = true, name = "Add Client Screen")
@Composable
fun AddEditClientScreenPreview() {
    TQDKhataTheme {
        AddEditClientContent()
    }
}

@Preview(showBackground = true, name = "Edit Client Screen")
@Composable
fun EditClientScreenPreview() {
    TQDKhataTheme {
        AddEditClientContent(
            initialClient = DemoData.sampleClient1
        )
    }
}

@Preview(showBackground = true, name = "Client Detail Screen")
@Composable
fun ClientDetailScreenPreview() {
    TQDKhataTheme {
        ClientDetailContent(
            client = DemoData.sampleClient1,
            reminders = DemoData.sampleReminders
        )
    }
}

@Preview(showBackground = true, name = "Client List Screen")
@Composable
fun ClientListScreenPreview() {
    TQDKhataTheme {
        ClientListContent(
            clients = DemoData.sampleClients,
            distinctCities = DemoData.distinctCities,
            totalCount = 4,
            leadsCount = 1,
            inProgressCount = 1,
            deliveredCount = 1,
            pendingRemindersCount = 2
        )
    }
}

@Preview(showBackground = true, name = "Reminders Screen")
@Composable
fun RemindersScreenPreview() {
    TQDKhataTheme {
        RemindersContent(
            reminders = DemoData.sampleRemindersWithClient
        )
    }
}

@Preview(showBackground = true, name = "Client Card")
@Composable
fun ClientCardPreview() {
    TQDKhataTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            ClientCard(
                client = DemoData.sampleClient1,
                onClick = {}
            )
        }
    }
}

@Preview(showBackground = true, name = "Status Badges")
@Composable
fun StatusBadgesPreview() {
    TQDKhataTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ProjectStatus.entries.forEach { status ->
                StatusBadge(status = status)
            }
        }
    }
}

@Preview(showBackground = true, name = "Priority Badges")
@Composable
fun PriorityBadgesPreview() {
    TQDKhataTheme {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Priority.entries.forEach { priority ->
                PriorityBadge(priority = priority)
            }
        }
    }
}

@Preview(showBackground = true, name = "Stat Summary Card")
@Composable
fun StatSummaryCardPreview() {
    TQDKhataTheme {
        StatSummaryCard(
            count = 12,
            label = "Total Clients",
            containerColor = BrandCream,
            contentColor = BrandNavy
        )
    }
}

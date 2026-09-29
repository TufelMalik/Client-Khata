package com.techquantum.tqdkhata

import com.techquantum.tqdkhata.data.model.ClientEntity
import com.techquantum.tqdkhata.data.model.Priority
import com.techquantum.tqdkhata.data.model.ProjectStatus
import com.techquantum.tqdkhata.data.model.ReminderEntity
import com.techquantum.tqdkhata.util.JsonBackupUtils
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test

class JsonBackupUtilsTest {

    @Test
    fun testExportAndParseRoundTrip() {
        val client1 = ClientEntity(
            id = 10L,
            name = "Test Client",
            phone = "9876543210",
            altPhone = "9123456789",
            email = "client@example.com",
            businessName = "Test Corp",
            businessType = "Software Development",
            requirements = "Building an Android app",
            budget = "₹50,000",
            status = ProjectStatus.IN_PROGRESS,
            priority = Priority.HIGH,
            address = "Station Road",
            city = "Bharuch",
            createdAt = 1700000000000L,
            updatedAt = 1700000050000L
        )

        val reminder1 = ReminderEntity(
            id = 100L,
            clientId = 10L,
            title = "Follow-up call",
            notes = "Discuss quotation and deliverables",
            reminderTimestamp = 1700001000000L,
            isCompleted = false,
            createdAt = 1700000020000L
        )

        val jsonString = JsonBackupUtils.createBackupJson(
            clients = listOf(client1),
            reminders = listOf(reminder1)
        )

        assertNotNull(jsonString)
        assertTrue(jsonString.contains("Test Client"))
        assertTrue(jsonString.contains("Follow-up call"))

        val parsed = JsonBackupUtils.parseBackupJson(jsonString)
        assertEquals(1, parsed.clients.size)
        assertEquals(1, parsed.reminders.size)

        val parsedClient = parsed.clients[0]
        assertEquals(10L, parsedClient.id)
        assertEquals("Test Client", parsedClient.name)
        assertEquals("9876543210", parsedClient.phone)
        assertEquals("9123456789", parsedClient.altPhone)
        assertEquals("client@example.com", parsedClient.email)
        assertEquals("Test Corp", parsedClient.businessName)
        assertEquals("Software Development", parsedClient.businessType)
        assertEquals("Building an Android app", parsedClient.requirements)
        assertEquals("₹50,000", parsedClient.budget)
        assertEquals(ProjectStatus.IN_PROGRESS, parsedClient.status)
        assertEquals(Priority.HIGH, parsedClient.priority)
        assertEquals("Station Road", parsedClient.address)
        assertEquals("Bharuch", parsedClient.city)
        assertEquals(1700000000000L, parsedClient.createdAt)

        val parsedReminder = parsed.reminders[0]
        assertEquals(100L, parsedReminder.id)
        assertEquals(10L, parsedReminder.clientId)
        assertEquals("Follow-up call", parsedReminder.title)
        assertEquals("Discuss quotation and deliverables", parsedReminder.notes)
        assertEquals(1700001000000L, parsedReminder.reminderTimestamp)
        assertEquals(false, parsedReminder.isCompleted)
    }

    @Test
    fun testParseBareClientsArray() {
        val jsonArrayString = """
            [
                {
                    "id": 1,
                    "name": "Single Array Client",
                    "phone": "9998887776",
                    "requirements": "Logo design",
                    "status": "NEW_LEAD",
                    "priority": "LOW"
                }
            ]
        """.trimIndent()

        val parsed = JsonBackupUtils.parseBackupJson(jsonArrayString)
        assertEquals(1, parsed.clients.size)
        assertEquals(0, parsed.reminders.size)
        assertEquals("Single Array Client", parsed.clients[0].name)
        assertEquals("9998887776", parsed.clients[0].phone)
        assertEquals(ProjectStatus.NEW_LEAD, parsed.clients[0].status)
        assertEquals(Priority.LOW, parsed.clients[0].priority)
    }

    @Test
    fun testHandlesNullAndMissingFieldsGracefully() {
        val jsonString = """
            {
                "version": 1,
                "clients": [
                    {
                        "name": "Minimal Client",
                        "phone": "1234567890",
                        "requirements": "Website",
                        "altPhone": null,
                        "email": "null",
                        "businessName": "",
                        "status": "UNKNOWN_STATUS",
                        "priority": "INVALID_PRIORITY"
                    }
                ],
                "reminders": [
                    {
                        "clientId": 1,
                        "title": "Minimal Reminder",
                        "notes": null,
                        "reminderTimestamp": null
                    }
                ]
            }
        """.trimIndent()

        val parsed = JsonBackupUtils.parseBackupJson(jsonString)
        assertEquals(1, parsed.clients.size)
        assertEquals(1, parsed.reminders.size)

        val client = parsed.clients[0]
        assertEquals("Minimal Client", client.name)
        assertNull(client.altPhone)
        assertNull(client.email)
        assertNull(client.businessName)
        assertNull(client.businessType)
        assertEquals(ProjectStatus.NEW_LEAD, client.status) // Fallback
        assertEquals(Priority.MEDIUM, client.priority) // Fallback

        val reminder = parsed.reminders[0]
        assertEquals("Minimal Reminder", reminder.title)
        assertNull(reminder.notes)
        assertNull(reminder.reminderTimestamp)
    }

    @Test
    fun testInvalidJsonThrowsException() {
        try {
            JsonBackupUtils.parseBackupJson("not valid json at all")
            fail("Expected IllegalArgumentException")
        } catch (e: IllegalArgumentException) {
            assertTrue(e.message?.contains("Invalid format") == true)
        }

        try {
            JsonBackupUtils.parseBackupJson("")
            fail("Expected IllegalArgumentException on empty string")
        } catch (e: IllegalArgumentException) {
            assertTrue(e.message?.contains("empty") == true)
        }
    }
}

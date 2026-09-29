package com.techquantum.tqdkhata

import com.techquantum.tqdkhata.receiver.ReminderNotificationReceiver
import com.techquantum.tqdkhata.util.ReminderAlarmScheduler
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ReminderAlarmSchedulerTest {

    @Test
    fun testSchedulerConstants() {
        assertEquals("tqd_khata_reminders", ReminderAlarmScheduler.CHANNEL_ID)
        assertEquals("extra_reminder_id", ReminderAlarmScheduler.EXTRA_REMINDER_ID)
        assertEquals("extra_client_id", ReminderAlarmScheduler.EXTRA_CLIENT_ID)
        assertEquals("extra_client_name", ReminderAlarmScheduler.EXTRA_CLIENT_NAME)
        assertEquals("extra_client_phone", ReminderAlarmScheduler.EXTRA_CLIENT_PHONE)
        assertEquals("extra_title", ReminderAlarmScheduler.EXTRA_TITLE)
        assertEquals("extra_notes", ReminderAlarmScheduler.EXTRA_NOTES)

        assertEquals("com.techquantum.tqdkhata.ACTION_REMINDER_ALERT", ReminderNotificationReceiver.ACTION_REMINDER_ALERT)
        assertEquals("com.techquantum.tqdkhata.ACTION_MARK_DONE", ReminderNotificationReceiver.ACTION_MARK_DONE)
    }

    @Test
    fun testRequestCodeGenerationIsPositive() {
        val reminderIds = listOf(1L, 42L, 999999L, Long.MAX_VALUE, 1234567890123L)
        for (id in reminderIds) {
            val requestCode = (id.hashCode().toLong() and 0x7FFFFFFF).toInt()
            assertTrue("Request code must be non-negative", requestCode >= 0)
        }
    }
}

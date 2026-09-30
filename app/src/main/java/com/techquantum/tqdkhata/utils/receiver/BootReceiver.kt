package com.techquantum.tqdkhata.utils.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.techquantum.tqdkhata.model.data.local.AppDatabase
import com.techquantum.tqdkhata.utils.helpers.ReminderAlarmScheduler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BootReceiver : BroadcastReceiver() {

    companion object {
        private const val TAG = "BootReceiver"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action ?: return
        if (action == Intent.ACTION_BOOT_COMPLETED ||
            action == "android.intent.action.QUICKBOOT_POWERON" ||
            action == "com.htc.intent.action.QUICKBOOT_POWERON" ||
            action == Intent.ACTION_MY_PACKAGE_REPLACED
        ) {
            Log.d(TAG, "Rescheduling active reminders after system boot or package update ($action)")
            val pendingResult = goAsync()
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val db = AppDatabase.getDatabase(context)
                    val activeReminders = db.reminderDao().getActiveRemindersAfter(System.currentTimeMillis())
                    Log.d(TAG, "Found ${activeReminders.size} active reminders to reschedule")
                    for (reminder in activeReminders) {
                        val triggerTime = reminder.reminderTimestamp ?: continue
                        ReminderAlarmScheduler.scheduleReminder(
                            context = context,
                            reminderId = reminder.id,
                            clientId = reminder.clientId,
                            clientName = reminder.clientName,
                            clientPhone = reminder.clientPhone,
                            title = reminder.title,
                            notes = reminder.notes,
                            triggerTime = triggerTime
                        )
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Error rescheduling reminders on boot: ${e.message}", e)
                } finally {
                    pendingResult.finish()
                }
            }
        }
    }
}

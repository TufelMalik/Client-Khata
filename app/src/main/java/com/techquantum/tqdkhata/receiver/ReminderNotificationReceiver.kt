package com.techquantum.tqdkhata.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import com.techquantum.tqdkhata.data.local.AppDatabase
import com.techquantum.tqdkhata.util.ReminderAlarmScheduler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ReminderNotificationReceiver : BroadcastReceiver() {

    companion object {
        private const val TAG = "ReminderReceiver"
        const val ACTION_REMINDER_ALERT = "com.techquantum.tqdkhata.ACTION_REMINDER_ALERT"
        const val ACTION_MARK_DONE = "com.techquantum.tqdkhata.ACTION_MARK_DONE"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action ?: return
        Log.d(TAG, "Received broadcast action: $action")

        when (action) {
            ACTION_REMINDER_ALERT -> {
                val reminderId = intent.getLongExtra(ReminderAlarmScheduler.EXTRA_REMINDER_ID, -1L)
                val clientId = intent.getLongExtra(ReminderAlarmScheduler.EXTRA_CLIENT_ID, -1L)
                var clientName = intent.getStringExtra(ReminderAlarmScheduler.EXTRA_CLIENT_NAME) ?: ""
                var clientPhone = intent.getStringExtra(ReminderAlarmScheduler.EXTRA_CLIENT_PHONE) ?: ""
                val title = intent.getStringExtra(ReminderAlarmScheduler.EXTRA_TITLE) ?: "Reminder Alert"
                val notes = intent.getStringExtra(ReminderAlarmScheduler.EXTRA_NOTES)

                if (reminderId <= 0L) return

                val pendingResult = goAsync()
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val db = AppDatabase.getDatabase(context)
                        val reminder = db.reminderDao().getReminderWithClientById(reminderId)
                        // If reminder exists and is already completed, do not alert
                        if (reminder != null && reminder.isCompleted) {
                            Log.d(TAG, "Reminder $reminderId is already completed; skipping notification")
                            return@launch
                        }

                        if (reminder != null) {
                            clientName = reminder.clientName
                            clientPhone = reminder.clientPhone
                        }

                        ReminderAlarmScheduler.showReminderNotification(
                            context = context,
                            reminderId = reminderId,
                            clientId = clientId,
                            clientName = clientName,
                            clientPhone = clientPhone,
                            title = reminder?.title ?: title,
                            notes = reminder?.notes ?: notes
                        )
                    } catch (e: Exception) {
                        Log.e(TAG, "Error in ReminderNotificationReceiver: ${e.message}", e)
                    } finally {
                        pendingResult.finish()
                    }
                }
            }

            ACTION_MARK_DONE -> {
                val reminderId = intent.getLongExtra(ReminderAlarmScheduler.EXTRA_REMINDER_ID, -1L)
                if (reminderId <= 0L) return

                val pendingResult = goAsync()
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val db = AppDatabase.getDatabase(context)
                        db.reminderDao().setCompletionStatus(reminderId, true)
                        val notifId = ((reminderId.hashCode().toLong() and 0x7FFFFFFF)).toInt()
                        val notificationManager = NotificationManagerCompat.from(context)
                        notificationManager.cancel(notifId)
                        Log.d(TAG, "Marked reminder $reminderId as completed and dismissed notification")
                    } catch (e: Exception) {
                        Log.e(TAG, "Error marking reminder done: ${e.message}", e)
                    } finally {
                        pendingResult.finish()
                    }
                }
            }
        }
    }
}

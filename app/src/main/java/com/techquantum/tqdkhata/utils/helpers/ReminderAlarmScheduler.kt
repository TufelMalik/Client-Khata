package com.techquantum.tqdkhata.utils.helpers

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.techquantum.tqdkhata.R
import com.techquantum.tqdkhata.app.MainActivity
import com.techquantum.tqdkhata.utils.receiver.ReminderNotificationReceiver

object ReminderAlarmScheduler {
    private const val TAG = "ReminderScheduler"
    const val CHANNEL_ID = "tqd_khata_reminders"
    const val CHANNEL_NAME = "Client Reminders & Follow-ups"

    const val EXTRA_REMINDER_ID = "extra_reminder_id"
    const val EXTRA_CLIENT_ID = "extra_client_id"
    const val EXTRA_CLIENT_NAME = "extra_client_name"
    const val EXTRA_CLIENT_PHONE = "extra_client_phone"
    const val EXTRA_TITLE = "extra_title"
    const val EXTRA_NOTES = "extra_notes"

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
                ?: return

            val soundUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION_EVENT)
                .build()

            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Alerts for scheduled client follow-ups and task reminders"
                enableLights(true)
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 500, 250, 500)
                setSound(soundUri, audioAttributes)
                lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC
            }

            notificationManager.createNotificationChannel(channel)
        }
    }

    fun scheduleReminder(
        context: Context,
        reminderId: Long,
        clientId: Long,
        clientName: String,
        clientPhone: String,
        title: String,
        notes: String?,
        triggerTime: Long
    ) {
        if (triggerTime <= System.currentTimeMillis()) {
            Log.d(TAG, "Cannot schedule reminder in the past (reminderId=$reminderId)")
            return
        }

        createNotificationChannel(context)

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return

        val intent = Intent(context, ReminderNotificationReceiver::class.java).apply {
            action = ReminderNotificationReceiver.ACTION_REMINDER_ALERT
            putExtra(EXTRA_REMINDER_ID, reminderId)
            putExtra(EXTRA_CLIENT_ID, clientId)
            putExtra(EXTRA_CLIENT_NAME, clientName)
            putExtra(EXTRA_CLIENT_PHONE, clientPhone)
            putExtra(EXTRA_TITLE, title)
            putExtra(EXTRA_NOTES, notes)
        }

        val requestCode = ((reminderId.hashCode().toLong() and 0x7FFFFFFF)).toInt()
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        triggerTime,
                        pendingIntent
                    )
                } else {
                    alarmManager.setAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        triggerTime,
                        pendingIntent
                    )
                }
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerTime,
                    pendingIntent
                )
            } else {
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    triggerTime,
                    pendingIntent
                )
            }
            Log.d(TAG, "Scheduled alarm for reminderId=$reminderId at $triggerTime")
        } catch (e: SecurityException) {
            Log.w(TAG, "SecurityException on exact alarm, using fallback: ${e.message}")
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarmManager.setAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        triggerTime,
                        pendingIntent
                    )
                } else {
                    alarmManager.set(
                        AlarmManager.RTC_WAKEUP,
                        triggerTime,
                        pendingIntent
                    )
                }
            } catch (fallbackEx: Exception) {
                Log.e(TAG, "Fallback alarm scheduling error: ${fallbackEx.message}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed scheduling alarm for reminderId=$reminderId: ${e.message}")
        }
    }

    fun cancelReminder(context: Context, reminderId: Long) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
        val intent = Intent(context, ReminderNotificationReceiver::class.java).apply {
            action = ReminderNotificationReceiver.ACTION_REMINDER_ALERT
        }
        val requestCode = ((reminderId.hashCode().toLong() and 0x7FFFFFFF)).toInt()
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )
        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent)
            pendingIntent.cancel()
            Log.d(TAG, "Cancelled alarm for reminderId=$reminderId")
        }
    }

    fun showReminderNotification(
        context: Context,
        reminderId: Long,
        clientId: Long,
        clientName: String,
        clientPhone: String,
        title: String,
        notes: String?
    ) {
        createNotificationChannel(context)

        val contentIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            putExtra("clientId", clientId)
            putExtra("reminderId", reminderId)
        }
        val notifId = ((reminderId.hashCode().toLong() and 0x7FFFFFFF)).toInt()
        val contentPendingIntent = PendingIntent.getActivity(
            context,
            notifId,
            contentIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val soundUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val displayTitle = if (clientName.isNotBlank()) "Reminder: $clientName" else "Follow-up Reminder"
        val bigText = buildString {
            append(title)
            if (!notes.isNullOrBlank()) {
                append("\n\nNote: ")
                append(notes)
            }
        }

        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification_bell)
            .setContentTitle(displayTitle)
            .setContentText(title)
            .setSubText("TQD Khata")
            .setStyle(NotificationCompat.BigTextStyle().bigText(bigText))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setAutoCancel(true)
            .setSound(soundUri)
            .setVibrate(longArrayOf(0, 500, 250, 500))
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setContentIntent(contentPendingIntent)

        if (clientPhone.isNotBlank()) {
            val callIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$clientPhone")).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            val callPendingIntent = PendingIntent.getActivity(
                context,
                notifId + 1000000,
                callIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            notificationBuilder.addAction(
                android.R.drawable.ic_menu_call,
                "Call",
                callPendingIntent
            )
        }

        val doneIntent = Intent(context, ReminderNotificationReceiver::class.java).apply {
            action = ReminderNotificationReceiver.ACTION_MARK_DONE
            putExtra(EXTRA_REMINDER_ID, reminderId)
        }
        val donePendingIntent = PendingIntent.getBroadcast(
            context,
            notifId + 2000000,
            doneIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        notificationBuilder.addAction(
            android.R.drawable.checkbox_on_background,
            "Mark Done",
            donePendingIntent
        )

        try {
            val notificationManager = NotificationManagerCompat.from(context)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(
                        context,
                        android.Manifest.permission.POST_NOTIFICATIONS
                    ) == android.content.pm.PackageManager.PERMISSION_GRANTED
                ) {
                    notificationManager.notify(notifId, notificationBuilder.build())
                } else {
                    Log.w(TAG, "Cannot show notification: POST_NOTIFICATIONS not granted")
                }
            } else {
                notificationManager.notify(notifId, notificationBuilder.build())
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error displaying notification: ${e.message}", e)
        }
    }
}

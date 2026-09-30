package com.techquantum.tqdkhata.utils.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

class ReminderSyncService : Service() {

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("ReminderSyncService", "ReminderSyncService triggered")
        return START_NOT_STICKY
    }
}

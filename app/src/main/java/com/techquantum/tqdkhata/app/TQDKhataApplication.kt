package com.techquantum.tqdkhata.app

import android.app.Application
import com.techquantum.tqdkhata.di.AppContainer
import com.techquantum.tqdkhata.utils.helpers.ReminderAlarmScheduler

class TQDKhataApplication : Application() {

    lateinit var appContainer: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        appContainer = AppContainer(this)
        ReminderAlarmScheduler.createNotificationChannel(this)
    }
}

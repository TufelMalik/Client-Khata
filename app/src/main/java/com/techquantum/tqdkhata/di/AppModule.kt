package com.techquantum.tqdkhata.di

import android.content.Context
import com.techquantum.tqdkhata.model.data.local.AppDatabase
import com.techquantum.tqdkhata.model.data.local.ClientDao
import com.techquantum.tqdkhata.model.data.local.ClientResourceDao
import com.techquantum.tqdkhata.model.data.local.ReminderDao

interface AppModule {
    val database: AppDatabase
    val clientDao: ClientDao
    val reminderDao: ReminderDao
    val clientResourceDao: ClientResourceDao
}

class AppModuleImpl(private val context: Context) : AppModule {
    override val database: AppDatabase by lazy {
        AppDatabase.getDatabase(context.applicationContext)
    }

    override val clientDao: ClientDao by lazy {
        database.clientDao()
    }

    override val reminderDao: ReminderDao by lazy {
        database.reminderDao()
    }

    override val clientResourceDao: ClientResourceDao by lazy {
        database.clientResourceDao()
    }
}

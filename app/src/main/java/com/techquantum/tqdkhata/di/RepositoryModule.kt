package com.techquantum.tqdkhata.di

import android.content.Context
import com.techquantum.tqdkhata.domain.repository.ClientRepository
import com.techquantum.tqdkhata.repository.ClientRepositoryImpl

interface RepositoryModule {
    val clientRepository: ClientRepository
}

class RepositoryModuleImpl(
    private val context: Context,
    private val appModule: AppModule
) : RepositoryModule {
    override val clientRepository: ClientRepository by lazy {
        ClientRepositoryImpl(
            clientDao = appModule.clientDao,
            reminderDao = appModule.reminderDao,
            clientResourceDao = appModule.clientResourceDao,
            database = appModule.database,
            context = context.applicationContext
        )
    }
}

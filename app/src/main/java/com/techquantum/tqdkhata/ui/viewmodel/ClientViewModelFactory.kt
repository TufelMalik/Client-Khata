package com.techquantum.tqdkhata.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.techquantum.tqdkhata.data.local.AppDatabase
import com.techquantum.tqdkhata.data.repository.ClientRepository

class ClientViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ClientViewModel::class.java)) {
            val db = AppDatabase.getDatabase(context.applicationContext)
            val repository = ClientRepository(db.clientDao(), db.reminderDao(), db.clientResourceDao(), db, context.applicationContext)
            return ClientViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

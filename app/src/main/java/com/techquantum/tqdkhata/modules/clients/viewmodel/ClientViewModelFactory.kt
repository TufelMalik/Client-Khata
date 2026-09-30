package com.techquantum.tqdkhata.modules.clients.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.techquantum.tqdkhata.di.AppContainer

class ClientViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val appContainer = AppContainer(context.applicationContext)
        val repository = appContainer.repositoryModule.clientRepository

        if (modelClass.isAssignableFrom(ClientViewModel::class.java)) {
            return ClientViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(ClientsViewModel::class.java)) {
            return ClientsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}

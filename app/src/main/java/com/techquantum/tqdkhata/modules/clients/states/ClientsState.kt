package com.techquantum.tqdkhata.modules.clients.states

import com.techquantum.tqdkhata.model.data.local.ClientEntity
import com.techquantum.tqdkhata.model.enums.ProjectStatus

data class ClientsState(
    val clients: List<ClientEntity> = emptyList(),
    val searchQuery: String = "",
    val selectedCity: String? = null,
    val selectedStatus: ProjectStatus? = null,
    val distinctCities: List<String> = emptyList(),
    val totalCount: Int = 0,
    val leadsCount: Int = 0,
    val inProgressCount: Int = 0,
    val deliveredCount: Int = 0,
    val pendingRemindersCount: Int = 0,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

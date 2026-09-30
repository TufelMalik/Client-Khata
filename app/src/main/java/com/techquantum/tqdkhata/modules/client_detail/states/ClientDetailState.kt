package com.techquantum.tqdkhata.modules.client_detail.states

import com.techquantum.tqdkhata.model.data.local.ClientEntity
import com.techquantum.tqdkhata.model.data.local.ClientResourceEntity
import com.techquantum.tqdkhata.model.data.local.ReminderEntity

data class ClientDetailState(
    val client: ClientEntity? = null,
    val reminders: List<ReminderEntity> = emptyList(),
    val resources: List<ClientResourceEntity> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

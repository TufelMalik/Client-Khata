package com.techquantum.tqdkhata.modules.client_form.states

import com.techquantum.tqdkhata.model.enums.Priority
import com.techquantum.tqdkhata.model.enums.ProjectStatus

data class ClientFormState(
    val id: Long = 0,
    val name: String = "",
    val phone: String = "",
    val altPhone: String = "",
    val email: String = "",
    val businessName: String = "",
    val businessType: String = "",
    val requirements: String = "",
    val budget: String = "",
    val status: ProjectStatus = ProjectStatus.NEW_LEAD,
    val priority: Priority = Priority.MEDIUM,
    val address: String = "",
    val city: String = "Bharuch",
    val isLoading: Boolean = false,
    val error: String? = null
)

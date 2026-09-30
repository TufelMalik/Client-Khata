package com.techquantum.tqdkhata.modules.client_form.states

import com.techquantum.tqdkhata.model.enums.Priority
import com.techquantum.tqdkhata.model.enums.ProjectStatus

sealed interface ClientFormAction {
    data class NameChanged(val name: String) : ClientFormAction
    data class PhoneChanged(val phone: String) : ClientFormAction
    data class RequirementsChanged(val requirements: String) : ClientFormAction
    data class StatusChanged(val status: ProjectStatus) : ClientFormAction
    data class PriorityChanged(val priority: Priority) : ClientFormAction
    data object Submit : ClientFormAction
}

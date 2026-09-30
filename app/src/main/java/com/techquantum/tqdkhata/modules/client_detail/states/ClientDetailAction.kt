package com.techquantum.tqdkhata.modules.client_detail.states

import com.techquantum.tqdkhata.model.data.local.ClientResourceEntity
import com.techquantum.tqdkhata.model.data.local.ReminderEntity
import com.techquantum.tqdkhata.model.enums.ProjectStatus

sealed interface ClientDetailAction {
    data class UpdateStatus(val newStatus: ProjectStatus) : ClientDetailAction
    data class DeleteReminder(val reminder: ReminderEntity) : ClientDetailAction
    data class ToggleReminder(val reminderId: Long, val completed: Boolean) : ClientDetailAction
    data class SaveResource(val resource: ClientResourceEntity) : ClientDetailAction
    data class DeleteResource(val resource: ClientResourceEntity) : ClientDetailAction
    data object DeleteClient : ClientDetailAction
}

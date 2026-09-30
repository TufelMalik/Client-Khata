package com.techquantum.tqdkhata.modules.reminders.states

sealed interface RemindersAction {
    data class ToggleReminder(val reminderId: Long, val completed: Boolean) : RemindersAction
    data class SetFilterPendingOnly(val pendingOnly: Boolean) : RemindersAction
}

package com.techquantum.tqdkhata.modules.reminders.states

sealed interface RemindersEvent {
    data class ShowToast(val message: String) : RemindersEvent
    data class NavigateToClient(val clientId: Long) : RemindersEvent
}

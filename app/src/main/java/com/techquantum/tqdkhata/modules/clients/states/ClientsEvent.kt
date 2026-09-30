package com.techquantum.tqdkhata.modules.clients.states

sealed interface ClientsEvent {
    data class ShowSnackbar(val message: String) : ClientsEvent
    data class NavigateToDetail(val clientId: Long) : ClientsEvent
    data object NavigateToAddClient : ClientsEvent
    data object NavigateToReminders : ClientsEvent
}

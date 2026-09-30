package com.techquantum.tqdkhata.modules.client_detail.states

sealed interface ClientDetailEvent {
    data class ShowMessage(val message: String) : ClientDetailEvent
    data object NavigateBack : ClientDetailEvent
    data class NavigateToEdit(val clientId: Long) : ClientDetailEvent
}

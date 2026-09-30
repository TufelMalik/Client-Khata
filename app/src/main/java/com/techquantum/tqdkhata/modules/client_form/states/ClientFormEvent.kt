package com.techquantum.tqdkhata.modules.client_form.states

sealed interface ClientFormEvent {
    data class ShowToast(val message: String) : ClientFormEvent
    data object SavedSuccess : ClientFormEvent
    data object NavigateBack : ClientFormEvent
}

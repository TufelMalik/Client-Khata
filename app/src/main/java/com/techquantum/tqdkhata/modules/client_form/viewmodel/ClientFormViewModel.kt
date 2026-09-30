package com.techquantum.tqdkhata.modules.client_form.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techquantum.tqdkhata.domain.repository.ClientRepository
import com.techquantum.tqdkhata.model.data.local.ClientEntity
import com.techquantum.tqdkhata.modules.client_form.states.ClientFormAction
import com.techquantum.tqdkhata.modules.client_form.states.ClientFormEvent
import com.techquantum.tqdkhata.modules.client_form.states.ClientFormState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ClientFormViewModel(
    private val repository: ClientRepository,
    private val clientId: Long? = null
) : ViewModel() {

    private val _state = MutableStateFlow(ClientFormState())
    val state = _state.asStateFlow()

    private val _events = Channel<ClientFormEvent>()
    val events = _events.receiveAsFlow()

    init {
        if (clientId != null && clientId > 0L) {
            viewModelScope.launch {
                repository.getClientById(clientId).collect { client ->
                    client?.let { c ->
                        _state.update {
                            it.copy(
                                id = c.id,
                                name = c.name,
                                phone = c.phone,
                                altPhone = c.altPhone ?: "",
                                email = c.email ?: "",
                                businessName = c.businessName ?: "",
                                businessType = c.businessType ?: "",
                                requirements = c.requirements,
                                budget = c.budget ?: "",
                                status = c.status,
                                priority = c.priority,
                                address = c.address ?: "",
                                city = c.city ?: "Bharuch"
                            )
                        }
                    }
                }
            }
        }
    }

    fun onAction(action: ClientFormAction) {
        when (action) {
            is ClientFormAction.NameChanged -> _state.update { it.copy(name = action.name) }
            is ClientFormAction.PhoneChanged -> _state.update { it.copy(phone = action.phone) }
            is ClientFormAction.RequirementsChanged -> _state.update { it.copy(requirements = action.requirements) }
            is ClientFormAction.StatusChanged -> _state.update { it.copy(status = action.status) }
            is ClientFormAction.PriorityChanged -> _state.update { it.copy(priority = action.priority) }
            is ClientFormAction.Submit -> {
                val current = _state.value
                if (current.name.isBlank()) {
                    viewModelScope.launch { _events.send(ClientFormEvent.ShowToast("Name is required")) }
                    return
                }
                if (current.phone.isBlank()) {
                    viewModelScope.launch { _events.send(ClientFormEvent.ShowToast("Phone is required")) }
                    return
                }
                viewModelScope.launch {
                    repository.saveClient(
                        ClientEntity(
                            id = current.id,
                            name = current.name.trim(),
                            phone = current.phone.trim(),
                            altPhone = current.altPhone.trim().ifBlank { null },
                            email = current.email.trim().ifBlank { null },
                            businessName = current.businessName.trim().ifBlank { null },
                            businessType = current.businessType.trim().ifBlank { null },
                            requirements = current.requirements.trim(),
                            budget = current.budget.trim().ifBlank { null },
                            status = current.status,
                            priority = current.priority,
                            address = current.address.trim().ifBlank { null },
                            city = current.city.trim().ifBlank { "Bharuch" }
                        )
                    )
                    _events.send(ClientFormEvent.SavedSuccess)
                }
            }
        }
    }
}

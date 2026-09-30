package com.techquantum.tqdkhata.modules.clients.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techquantum.tqdkhata.domain.repository.ClientRepository
import com.techquantum.tqdkhata.modules.clients.states.ClientsAction
import com.techquantum.tqdkhata.modules.clients.states.ClientsEvent
import com.techquantum.tqdkhata.modules.clients.states.ClientsState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ClientsViewModel(
    private val repository: ClientRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ClientsState())
    val state = _state.asStateFlow()

    private val _eventChannel = Channel<ClientsEvent>()
    val events = _eventChannel.receiveAsFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            repository.getAllClients().collect { clientList ->
                _state.update { it.copy(clients = clientList) }
            }
        }
    }

    fun onAction(action: ClientsAction) {
        when (action) {
            is ClientsAction.SearchQueryChanged -> {
                _state.update { it.copy(searchQuery = action.query) }
            }
            is ClientsAction.CitySelected -> {
                _state.update { it.copy(selectedCity = action.city) }
            }
            is ClientsAction.StatusSelected -> {
                _state.update { it.copy(selectedStatus = action.status) }
            }
            is ClientsAction.ClearFilters -> {
                _state.update { it.copy(searchQuery = "", selectedCity = null, selectedStatus = null) }
            }
            is ClientsAction.ExportRequested -> {
                // Triggered from UI
            }
            is ClientsAction.ImportRequested -> {
                // Triggered from UI
            }
        }
    }
}

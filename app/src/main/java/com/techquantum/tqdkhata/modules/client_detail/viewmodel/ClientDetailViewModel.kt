package com.techquantum.tqdkhata.modules.client_detail.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techquantum.tqdkhata.domain.repository.ClientRepository
import com.techquantum.tqdkhata.modules.client_detail.states.ClientDetailAction
import com.techquantum.tqdkhata.modules.client_detail.states.ClientDetailEvent
import com.techquantum.tqdkhata.modules.client_detail.states.ClientDetailState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ClientDetailViewModel(
    private val repository: ClientRepository,
    private val clientId: Long
) : ViewModel() {

    private val _state = MutableStateFlow(ClientDetailState(isLoading = true))
    val state = _state.asStateFlow()

    private val _events = Channel<ClientDetailEvent>()
    val events = _events.receiveAsFlow()

    init {
        loadClientDetails()
    }

    private fun loadClientDetails() {
        viewModelScope.launch {
            repository.getClientById(clientId).collect { client ->
                _state.update { it.copy(client = client, isLoading = false) }
            }
        }
        viewModelScope.launch {
            repository.getRemindersForClient(clientId).collect { reminders ->
                _state.update { it.copy(reminders = reminders) }
            }
        }
        viewModelScope.launch {
            repository.getResourcesForClient(clientId).collect { resources ->
                _state.update { it.copy(resources = resources) }
            }
        }
    }

    fun onAction(action: ClientDetailAction) {
        when (action) {
            is ClientDetailAction.UpdateStatus -> {
                val current = _state.value.client ?: return
                viewModelScope.launch {
                    repository.saveClient(current.copy(status = action.newStatus))
                }
            }
            is ClientDetailAction.DeleteReminder -> {
                viewModelScope.launch {
                    repository.deleteReminder(action.reminder)
                }
            }
            is ClientDetailAction.ToggleReminder -> {
                viewModelScope.launch {
                    repository.setReminderCompletion(action.reminderId, action.completed)
                }
            }
            is ClientDetailAction.SaveResource -> {
                viewModelScope.launch {
                    repository.saveResource(action.resource)
                }
            }
            is ClientDetailAction.DeleteResource -> {
                viewModelScope.launch {
                    repository.deleteResource(action.resource)
                }
            }
            is ClientDetailAction.DeleteClient -> {
                val current = _state.value.client ?: return
                viewModelScope.launch {
                    repository.deleteClient(current)
                    _events.send(ClientDetailEvent.NavigateBack)
                }
            }
        }
    }
}

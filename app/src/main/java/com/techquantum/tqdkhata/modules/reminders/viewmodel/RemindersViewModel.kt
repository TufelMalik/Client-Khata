package com.techquantum.tqdkhata.modules.reminders.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techquantum.tqdkhata.domain.repository.ClientRepository
import com.techquantum.tqdkhata.modules.reminders.states.RemindersAction
import com.techquantum.tqdkhata.modules.reminders.states.RemindersEvent
import com.techquantum.tqdkhata.modules.reminders.states.RemindersState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RemindersViewModel(
    private val repository: ClientRepository
) : ViewModel() {

    private val _state = MutableStateFlow(RemindersState(isLoading = true))
    val state = _state.asStateFlow()

    private val _events = Channel<RemindersEvent>()
    val events = _events.receiveAsFlow()

    init {
        loadReminders()
    }

    private fun loadReminders() {
        viewModelScope.launch {
            repository.getAllRemindersWithClient().collect { reminders ->
                _state.update { it.copy(reminders = reminders, isLoading = false) }
            }
        }
    }

    fun onAction(action: RemindersAction) {
        when (action) {
            is RemindersAction.ToggleReminder -> {
                viewModelScope.launch {
                    repository.setReminderCompletion(action.reminderId, action.completed)
                }
            }
            is RemindersAction.SetFilterPendingOnly -> {
                _state.update { it.copy(filterPendingOnly = action.pendingOnly) }
            }
        }
    }
}

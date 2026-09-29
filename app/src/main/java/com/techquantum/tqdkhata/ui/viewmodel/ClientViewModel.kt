package com.techquantum.tqdkhata.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techquantum.tqdkhata.data.model.BackupData
import com.techquantum.tqdkhata.data.model.ClientEntity
import com.techquantum.tqdkhata.data.model.ClientResourceEntity
import com.techquantum.tqdkhata.data.model.ImportMode
import com.techquantum.tqdkhata.data.model.ImportResult
import com.techquantum.tqdkhata.data.model.ProjectStatus
import com.techquantum.tqdkhata.data.model.ReminderEntity
import com.techquantum.tqdkhata.data.model.ReminderWithClient
import com.techquantum.tqdkhata.data.repository.ClientRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class ClientViewModel(
    private val repository: ClientRepository
) : ViewModel() {

    init {
        viewModelScope.launch {
            repository.cleanupAllVideoResources()
        }
    }

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCity = MutableStateFlow<String?>(null)
    val selectedCity: StateFlow<String?> = _selectedCity.asStateFlow()

    private val _selectedStatus = MutableStateFlow<ProjectStatus?>(null)
    val selectedStatus: StateFlow<ProjectStatus?> = _selectedStatus.asStateFlow()

    val clients: StateFlow<List<ClientEntity>> = combine(
        _searchQuery,
        _selectedCity,
        _selectedStatus
    ) { query, city, status ->
        Triple(query, city, status)
    }.flatMapLatest { (query, city, status) ->
        repository.filterClients(query, city, status)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val distinctCities: StateFlow<List<String>> = repository.getAllDistinctCities()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val totalCount: StateFlow<Int> = repository.getTotalClientCount()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

    val leadsCount: StateFlow<Int> = repository.getCountByStatus(ProjectStatus.NEW_LEAD)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

    val inProgressCount: StateFlow<Int> = repository.getCountByStatus(ProjectStatus.IN_PROGRESS)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

    val deliveredCount: StateFlow<Int> = repository.getCountByStatus(ProjectStatus.DELIVERED)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

    val reminders: StateFlow<List<ReminderWithClient>> = repository.getAllRemindersWithClient()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun onCitySelected(city: String?) {
        _selectedCity.value = city
    }

    fun onStatusSelected(status: ProjectStatus?) {
        _selectedStatus.value = if (_selectedStatus.value == status) null else status
    }

    fun getClient(id: Long): Flow<ClientEntity?> = repository.getClientById(id)

    fun getRemindersForClient(clientId: Long): Flow<List<ReminderEntity>> =
        repository.getRemindersForClient(clientId)

    fun saveClient(client: ClientEntity, onComplete: ((Long) -> Unit)? = null) {
        viewModelScope.launch {
            val id = repository.saveClient(client)
            onComplete?.invoke(id)
        }
    }

    fun updateClientStatus(client: ClientEntity, newStatus: ProjectStatus) {
        viewModelScope.launch {
            repository.saveClient(client.copy(status = newStatus))
        }
    }

    fun deleteClient(client: ClientEntity, onComplete: (() -> Unit)? = null) {
        viewModelScope.launch {
            repository.deleteClient(client)
            onComplete?.invoke()
        }
    }

    fun saveReminder(reminder: ReminderEntity, onComplete: (() -> Unit)? = null) {
        viewModelScope.launch {
            repository.saveReminder(reminder)
            onComplete?.invoke()
        }
    }

    fun toggleReminderCompletion(id: Long, completed: Boolean) {
        viewModelScope.launch {
            repository.setReminderCompletion(id, completed)
        }
    }

    fun deleteReminder(reminder: ReminderEntity) {
        viewModelScope.launch {
            repository.deleteReminder(reminder)
        }
    }

    fun getResourcesForClient(clientId: Long): Flow<List<ClientResourceEntity>> =
        repository.getResourcesForClient(clientId)

    fun saveResource(resource: ClientResourceEntity, onComplete: ((Long) -> Unit)? = null) {
        viewModelScope.launch {
            val id = repository.saveResource(resource)
            onComplete?.invoke(id)
        }
    }

    fun deleteResource(resource: ClientResourceEntity, onComplete: (() -> Unit)? = null) {
        viewModelScope.launch {
            repository.deleteResource(resource)
            onComplete?.invoke()
        }
    }

    fun exportDataToJson(
        onSuccess: (String) -> Unit,
        onError: (Throwable) -> Unit = {}
    ) {
        viewModelScope.launch {
            try {
                val json = repository.exportDataToJson()
                onSuccess(json)
            } catch (e: Throwable) {
                onError(e)
            }
        }
    }

    fun importBackupData(
        backupData: BackupData,
        onSuccess: (ImportResult) -> Unit,
        onError: (Throwable) -> Unit = {}
    ) {
        viewModelScope.launch {
            try {
                val result = repository.importBackupData(backupData)
                onSuccess(result)
            } catch (e: Throwable) {
                onError(e)
            }
        }
    }

    fun importDataFromJson(
        jsonData: String,
        onSuccess: (ImportResult) -> Unit,
        onError: (Throwable) -> Unit = {}
    ) {
        viewModelScope.launch {
            try {
                val result = repository.importDataFromJson(jsonData)
                onSuccess(result)
            } catch (e: Throwable) {
                onError(e)
            }
        }
    }
}

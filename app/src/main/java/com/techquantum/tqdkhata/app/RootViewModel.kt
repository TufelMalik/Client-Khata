package com.techquantum.tqdkhata.app

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class RootViewModel : ViewModel() {

    private val _pendingClientId = MutableStateFlow<Long?>(null)
    val pendingClientId: StateFlow<Long?> = _pendingClientId.asStateFlow()

    fun setPendingClientId(clientId: Long?) {
        _pendingClientId.value = clientId
    }

    fun clearPendingClientId() {
        _pendingClientId.value = null
    }
}

package com.techquantum.tqdkhata.modules.clients.states

import com.techquantum.tqdkhata.model.enums.ProjectStatus

sealed interface ClientsAction {
    data class SearchQueryChanged(val query: String) : ClientsAction
    data class CitySelected(val city: String?) : ClientsAction
    data class StatusSelected(val status: ProjectStatus?) : ClientsAction
    data object ClearFilters : ClientsAction
    data object ExportRequested : ClientsAction
    data object ImportRequested : ClientsAction
}

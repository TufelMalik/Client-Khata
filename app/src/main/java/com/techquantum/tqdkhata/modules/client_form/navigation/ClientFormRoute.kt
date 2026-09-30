package com.techquantum.tqdkhata.modules.client_form.navigation

object ClientFormRoute {
    const val ARG_CLIENT_ID = "clientId"
    const val ROUTE = "client_form?clientId={clientId}"

    fun createRoute(clientId: Long? = null): String =
        if (clientId != null && clientId > 0L) "client_form?clientId=$clientId" else "client_form"
}

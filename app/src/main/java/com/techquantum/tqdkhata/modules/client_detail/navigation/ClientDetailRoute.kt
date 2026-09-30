package com.techquantum.tqdkhata.modules.client_detail.navigation

object ClientDetailRoute {
    const val ARG_CLIENT_ID = "clientId"
    const val ROUTE = "client_detail/{clientId}"

    fun createRoute(clientId: Long): String = "client_detail/$clientId"
}

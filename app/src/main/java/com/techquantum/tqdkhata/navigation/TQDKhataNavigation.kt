package com.techquantum.tqdkhata.navigation

import androidx.navigation.NavController
import com.techquantum.tqdkhata.modules.client_detail.navigation.ClientDetailRoute
import com.techquantum.tqdkhata.modules.client_form.navigation.ClientFormRoute
import com.techquantum.tqdkhata.modules.clients.navigation.ClientsRoute
import com.techquantum.tqdkhata.modules.reminders.navigation.RemindersRoute

object TQDKhataDestinations {
    const val CLIENTS = ClientsRoute.ROUTE
    const val CLIENT_DETAIL = ClientDetailRoute.ROUTE
    const val CLIENT_FORM = ClientFormRoute.ROUTE
    const val REMINDERS = RemindersRoute.ROUTE
}

fun NavController.navigateToClientDetail(clientId: Long) {
    navigate(ClientDetailRoute.createRoute(clientId))
}

fun NavController.navigateToClientForm(clientId: Long? = null) {
    navigate(ClientFormRoute.createRoute(clientId))
}

fun NavController.navigateToReminders() {
    navigate(RemindersRoute.ROUTE)
}

fun NavController.navigateToTodaysFollowups() {
    navigate(RemindersRoute.ROUTE)
}


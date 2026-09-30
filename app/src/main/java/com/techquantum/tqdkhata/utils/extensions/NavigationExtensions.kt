package com.techquantum.tqdkhata.utils.extensions

import androidx.navigation.NavController

fun NavController.navigateSingleTop(route: String) {
    navigate(route) {
        launchSingleTop = true
    }
}

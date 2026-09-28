package com.techquantum.tqdkhata.data.model

enum class ProjectStatus(val displayName: String) {
    NEW_LEAD("New Lead"),
    IN_DISCUSSION("Under Meeting"),
    QUOTED("Quoted"),
    IN_PROGRESS("In Progress"),
    DELIVERED("Delivered"),
    ON_HOLD("On Hold"),
    CANCELLED("Cancelled");

    companion object {
        fun fromString(value: String?): ProjectStatus {
            return entries.firstOrNull { it.name.equals(value, ignoreCase = true) } ?: NEW_LEAD
        }
    }
}

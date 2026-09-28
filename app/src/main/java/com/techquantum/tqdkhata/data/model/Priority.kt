package com.techquantum.tqdkhata.data.model

enum class Priority(val label: String) {
    HIGH("High"),
    MEDIUM("Medium"),
    LOW("Low");

    companion object {
        fun fromString(value: String?): Priority {
            return entries.firstOrNull { it.name.equals(value, ignoreCase = true) } ?: MEDIUM
        }
    }
}

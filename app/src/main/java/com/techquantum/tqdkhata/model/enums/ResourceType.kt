package com.techquantum.tqdkhata.model.enums

enum class ResourceType(val label: String) {
    IMAGE("Image"),
    VIDEO("Video");

    companion object {
        fun fromString(value: String?): ResourceType {
            return entries.firstOrNull { it.name.equals(value, ignoreCase = true) } ?: IMAGE
        }
    }
}

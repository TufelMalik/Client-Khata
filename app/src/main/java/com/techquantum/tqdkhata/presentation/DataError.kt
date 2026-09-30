package com.techquantum.tqdkhata.presentation

sealed interface DataError {
    enum class Network : DataError {
        REQUEST_TIMEOUT,
        UNAUTHORIZED,
        NO_INTERNET,
        SERVER_ERROR,
        UNKNOWN
    }

    enum class Local : DataError {
        DISK_FULL,
        FILE_NOT_FOUND,
        PERMISSION_DENIED,
        INVALID_FORMAT,
        UNKNOWN
    }
}

fun Throwable.toDataError(): DataError {
    val msg = this.localizedMessage ?: this.message ?: ""
    return when {
        msg.contains("Permission", ignoreCase = true) || msg.contains("denied", ignoreCase = true) ->
            DataError.Local.PERMISSION_DENIED
        msg.contains("space", ignoreCase = true) || msg.contains("full", ignoreCase = true) ->
            DataError.Local.DISK_FULL
        msg.contains("not found", ignoreCase = true) ->
            DataError.Local.FILE_NOT_FOUND
        msg.contains("JSON", ignoreCase = true) || msg.contains("format", ignoreCase = true) ->
            DataError.Local.INVALID_FORMAT
        else -> DataError.Local.UNKNOWN
    }
}

fun DataError.toUiText(): UiText {
    return when (this) {
        DataError.Network.REQUEST_TIMEOUT -> UiText.DynamicString("Request timed out. Please try again.")
        DataError.Network.UNAUTHORIZED -> UiText.DynamicString("Unauthorized access.")
        DataError.Network.NO_INTERNET -> UiText.DynamicString("No internet connection available.")
        DataError.Network.SERVER_ERROR -> UiText.DynamicString("Server encountered an error.")
        DataError.Network.UNKNOWN -> UiText.DynamicString("An unexpected network error occurred.")
        DataError.Local.DISK_FULL -> UiText.DynamicString("Storage full. Please free up space.")
        DataError.Local.FILE_NOT_FOUND -> UiText.DynamicString("File not found.")
        DataError.Local.PERMISSION_DENIED -> UiText.DynamicString("Permission was denied.")
        DataError.Local.INVALID_FORMAT -> UiText.DynamicString("Invalid data format or corrupted JSON.")
        DataError.Local.UNKNOWN -> UiText.DynamicString("An unexpected error occurred.")
    }
}

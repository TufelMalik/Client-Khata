package com.techquantum.tqdkhata.model.response

sealed interface ApiResponse<out T> {
    data class Success<T>(val data: T) : ApiResponse<T>
    data class Error(val message: String, val code: Int = -1) : ApiResponse<Nothing>
    data object Loading : ApiResponse<Nothing>
}

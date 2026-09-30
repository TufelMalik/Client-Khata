package com.techquantum.tqdkhata.network

interface ApiService {
    suspend fun ping(): Result<Boolean>
}

class ApiServiceImpl : ApiService {
    override suspend fun ping(): Result<Boolean> = Result.success(true)
}

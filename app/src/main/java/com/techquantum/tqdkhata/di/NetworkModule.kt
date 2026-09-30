package com.techquantum.tqdkhata.di

import com.techquantum.tqdkhata.network.ApiService
import com.techquantum.tqdkhata.network.ApiServiceImpl
import com.techquantum.tqdkhata.network.LoggingInterceptor
import com.techquantum.tqdkhata.network.NetworkInterceptor

interface NetworkModule {
    val apiService: ApiService
    val loggingInterceptor: NetworkInterceptor
}

class NetworkModuleImpl : NetworkModule {
    override val apiService: ApiService by lazy {
        ApiServiceImpl()
    }

    override val loggingInterceptor: NetworkInterceptor by lazy {
        LoggingInterceptor()
    }
}

package com.techquantum.tqdkhata.network

import android.util.Log

interface NetworkInterceptor {
    fun intercept(url: String, method: String)
}

class LoggingInterceptor : NetworkInterceptor {
    override fun intercept(url: String, method: String) {
        Log.d("NetworkInterceptor", "HTTP $method: $url")
    }
}

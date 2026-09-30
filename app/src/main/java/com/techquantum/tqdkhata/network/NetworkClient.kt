package com.techquantum.tqdkhata.network

import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

object NetworkClient {
    private const val CONNECT_TIMEOUT_MS = 15000
    private const val READ_TIMEOUT_MS = 15000

    fun openConnection(urlString: String): HttpURLConnection {
        val url = URL(urlString)
        val connection = url.openConnection() as HttpURLConnection
        connection.connectTimeout = CONNECT_TIMEOUT_MS
        connection.readTimeout = READ_TIMEOUT_MS
        connection.setRequestProperty("Accept", "application/json")
        connection.setRequestProperty("User-Agent", "TQDKhata-Android")
        return connection
    }

    suspend fun get(urlString: String): Result<String> = runCatching {
        val connection = openConnection(urlString)
        connection.requestMethod = "GET"
        connection.connect()
        val stream: InputStream = if (connection.responseCode in 200..299) {
            connection.inputStream
        } else {
            connection.errorStream ?: connection.inputStream
        }
        val response = stream.bufferedReader().use { it.readText() }
        connection.disconnect()
        response
    }
}

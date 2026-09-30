package com.techquantum.tqdkhata.utils.helpers

import com.techquantum.tqdkhata.model.data.local.ClientEntity
import com.techquantum.tqdkhata.model.data.local.ReminderEntity
import com.techquantum.tqdkhata.model.enums.Priority
import com.techquantum.tqdkhata.model.enums.ProjectStatus
import com.techquantum.tqdkhata.model.response.BackupData

/**
 * Pure Kotlin JSON parser and serializer with ZERO external dependencies.
 * Does not require org.json, Gson, Jackson, Moshi, or kotlinx-serialization.
 */
object JsonBackupUtils {

    const val CURRENT_VERSION = 1
    const val APP_NAME = "TQD Khata"

    fun createBackupJson(clients: List<ClientEntity>, reminders: List<ReminderEntity>): String {
        val sb = StringBuilder()
        sb.append("{\n")
        sb.append("  \"version\": ").append(CURRENT_VERSION).append(",\n")
        sb.append("  \"appName\": \"").append(escapeJson(APP_NAME)).append("\",\n")
        sb.append("  \"exportedAt\": ").append(System.currentTimeMillis()).append(",\n")
        sb.append("  \"clientCount\": ").append(clients.size).append(",\n")
        sb.append("  \"reminderCount\": ").append(reminders.size).append(",\n")

        // Clients
        sb.append("  \"clients\": [\n")
        clients.forEachIndexed { index, client ->
            sb.append("    {\n")
            sb.append("      \"id\": ").append(client.id).append(",\n")
            sb.append("      \"name\": \"").append(escapeJson(client.name)).append("\",\n")
            sb.append("      \"phone\": \"").append(escapeJson(client.phone)).append("\",\n")
            sb.append("      \"altPhone\": ").append(client.altPhone?.let { "\"${escapeJson(it)}\"" } ?: "null").append(",\n")
            sb.append("      \"email\": ").append(client.email?.let { "\"${escapeJson(it)}\"" } ?: "null").append(",\n")
            sb.append("      \"businessName\": ").append(client.businessName?.let { "\"${escapeJson(it)}\"" } ?: "null").append(",\n")
            sb.append("      \"businessType\": ").append(client.businessType?.let { "\"${escapeJson(it)}\"" } ?: "null").append(",\n")
            sb.append("      \"requirements\": \"").append(escapeJson(client.requirements)).append("\",\n")
            sb.append("      \"budget\": ").append(client.budget?.let { "\"${escapeJson(it)}\"" } ?: "null").append(",\n")
            sb.append("      \"status\": \"").append(client.status.name).append("\",\n")
            sb.append("      \"priority\": \"").append(client.priority.name).append("\",\n")
            sb.append("      \"address\": ").append(client.address?.let { "\"${escapeJson(it)}\"" } ?: "null").append(",\n")
            sb.append("      \"city\": ").append(client.city?.let { "\"${escapeJson(it)}\"" } ?: "null").append(",\n")
            sb.append("      \"shopImagePath\": ").append(client.shopImagePath?.let { "\"${escapeJson(it)}\"" } ?: "null").append(",\n")
            sb.append("      \"createdAt\": ").append(client.createdAt).append(",\n")
            sb.append("      \"updatedAt\": ").append(client.updatedAt).append("\n")
            sb.append("    }")
            if (index < clients.size - 1) sb.append(",")
            sb.append("\n")
        }
        sb.append("  ],\n")

        // Reminders
        sb.append("  \"reminders\": [\n")
        reminders.forEachIndexed { index, reminder ->
            sb.append("    {\n")
            sb.append("      \"id\": ").append(reminder.id).append(",\n")
            sb.append("      \"clientId\": ").append(reminder.clientId).append(",\n")
            sb.append("      \"title\": \"").append(escapeJson(reminder.title)).append("\",\n")
            sb.append("      \"notes\": ").append(reminder.notes?.let { "\"${escapeJson(it)}\"" } ?: "null").append(",\n")
            sb.append("      \"reminderTimestamp\": ").append(reminder.reminderTimestamp?.toString() ?: "null").append(",\n")
            sb.append("      \"isCompleted\": ").append(reminder.isCompleted).append(",\n")
            sb.append("      \"createdAt\": ").append(reminder.createdAt).append("\n")
            sb.append("    }")
            if (index < reminders.size - 1) sb.append(",")
            sb.append("\n")
        }
        sb.append("  ]\n")

        sb.append("}")
        return sb.toString()
    }

    fun parseBackupJson(jsonString: String): BackupData {
        val trimmed = jsonString.trim()
        if (trimmed.isEmpty()) {
            throw IllegalArgumentException("The backup file is empty.")
        }

        val parsed = try {
            PureJsonParser.parse(trimmed)
        } catch (e: Exception) {
            throw IllegalArgumentException("Invalid format. The file is not a valid JSON document: ${e.message}", e)
        }
        val clients = mutableListOf<ClientEntity>()
        val reminders = mutableListOf<ReminderEntity>()
        var version = CURRENT_VERSION
        var appName = APP_NAME
        var exportedAt = System.currentTimeMillis()

        when (parsed) {
            is Map<*, *> -> {
                version = (parsed["version"] as? Number)?.toInt() ?: CURRENT_VERSION
                appName = parsed["appName"] as? String ?: APP_NAME
                exportedAt = (parsed["exportedAt"] as? Number)?.toLong() ?: System.currentTimeMillis()

                val clientsList = parsed["clients"] as? List<*>
                clientsList?.forEach { item ->
                    (item as? Map<*, *>)?.let { clientMap ->
                        clients.add(parseClientFromMap(clientMap))
                    }
                }

                val remindersList = parsed["reminders"] as? List<*>
                remindersList?.forEach { item ->
                    (item as? Map<*, *>)?.let { reminderMap ->
                        reminders.add(parseReminderFromMap(reminderMap))
                    }
                }
            }
            is List<*> -> {
                parsed.forEach { item ->
                    (item as? Map<*, *>)?.let { clientMap ->
                        clients.add(parseClientFromMap(clientMap))
                    }
                }
            }
            else -> {
                throw IllegalArgumentException("Invalid format. Root must be a JSON object or array.")
            }
        }

        return BackupData(
            version = version,
            appName = appName,
            exportedAt = exportedAt,
            clients = clients,
            reminders = reminders
        )
    }

    private fun parseClientFromMap(map: Map<*, *>): ClientEntity {
        val id = (map["id"] as? Number)?.toLong() ?: 0L
        val name = map.getNullableString("name") ?: "Unnamed Client"
        val phone = map.getNullableString("phone") ?: ""
        val altPhone = map.getNullableString("altPhone")
        val email = map.getNullableString("email")
        val businessName = map.getNullableString("businessName")
        val businessType = map.getNullableString("businessType")
        val requirements = map.getNullableString("requirements") ?: ""
        val budget = map.getNullableString("budget")
        val status = ProjectStatus.fromString(map.getNullableString("status"))
        val priority = Priority.fromString(map.getNullableString("priority"))
        val address = map.getNullableString("address")
        val city = map.getNullableString("city") ?: "Bharuch"
        val shopImagePath = map.getNullableString("shopImagePath")
        val createdAt = (map["createdAt"] as? Number)?.toLong() ?: System.currentTimeMillis()
        val updatedAt = (map["updatedAt"] as? Number)?.toLong() ?: System.currentTimeMillis()

        return ClientEntity(
            id = id,
            name = name,
            phone = phone,
            altPhone = altPhone,
            email = email,
            businessName = businessName,
            businessType = businessType,
            requirements = requirements,
            budget = budget,
            status = status,
            priority = priority,
            address = address,
            city = city,
            shopImagePath = shopImagePath,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }

    private fun parseReminderFromMap(map: Map<*, *>): ReminderEntity {
        val id = (map["id"] as? Number)?.toLong() ?: 0L
        val clientId = (map["clientId"] as? Number)?.toLong() ?: 0L
        val title = map.getNullableString("title") ?: "Reminder"
        val notes = map.getNullableString("notes")
        val reminderTimestamp = (map["reminderTimestamp"] as? Number)?.toLong()
        val isCompleted = (map["isCompleted"] as? Boolean) ?: false
        val createdAt = (map["createdAt"] as? Number)?.toLong() ?: System.currentTimeMillis()

        return ReminderEntity(
            id = id,
            clientId = clientId,
            title = title,
            notes = notes,
            reminderTimestamp = reminderTimestamp,
            isCompleted = isCompleted,
            createdAt = createdAt
        )
    }

    private fun Map<*, *>.getNullableString(key: String): String? {
        val value = this[key] ?: return null
        val str = value.toString().trim()
        return if (str.isEmpty() || str.equals("null", ignoreCase = true)) null else str
    }

    fun escapeJson(value: String): String {
        val sb = StringBuilder()
        for (c in value) {
            when (c) {
                '"' -> sb.append("\\\"")
                '\\' -> sb.append("\\\\")
                '\b' -> sb.append("\\b")
                '\u000C' -> sb.append("\\f")
                '\n' -> sb.append("\\n")
                '\r' -> sb.append("\\r")
                '\t' -> sb.append("\\t")
                else -> {
                    if (c.code in 0x00..0x1F) {
                        sb.append(String.format("\\u%04x", c.code))
                    } else {
                        sb.append(c)
                    }
                }
            }
        }
        return sb.toString()
    }
}

/**
 * Lightweight pure-Kotlin JSON parser without any external dependencies.
 */
internal object PureJsonParser {

    fun parse(json: String): Any? {
        val parser = Lexer(json)
        val result = parser.parseValue()
        parser.skipWhitespace()
        if (parser.hasMore()) {
            throw IllegalArgumentException("Unexpected character at position ${parser.index}: '${parser.currentChar()}'")
        }
        return result
    }

    private class Lexer(private val src: String) {
        var index = 0

        fun hasMore(): Boolean = index < src.length
        fun currentChar(): Char = src[index]

        fun skipWhitespace() {
            while (hasMore() && src[index].isWhitespace()) {
                index++
            }
        }

        fun parseValue(): Any? {
            skipWhitespace()
            if (!hasMore()) throw IllegalArgumentException("Unexpected end of JSON input")
            return when (val c = src[index]) {
                '{' -> parseObject()
                '[' -> parseArray()
                '"' -> parseString()
                't', 'f' -> parseBoolean()
                'n' -> parseNull()
                '-', in '0'..'9' -> parseNumber()
                else -> throw IllegalArgumentException("Unexpected token '$c' at position $index")
            }
        }

        private fun parseObject(): Map<String, Any?> {
            match('{')
            skipWhitespace()
            val map = mutableMapOf<String, Any?>()
            if (hasMore() && currentChar() == '}') {
                index++
                return map
            }
            while (hasMore()) {
                skipWhitespace()
                if (currentChar() != '"') {
                    throw IllegalArgumentException("Expected string key at position $index")
                }
                val key = parseString()
                skipWhitespace()
                match(':')
                val value = parseValue()
                map[key] = value
                skipWhitespace()
                if (hasMore() && currentChar() == ',') {
                    index++
                    continue
                } else if (hasMore() && currentChar() == '}') {
                    index++
                    break
                } else {
                    throw IllegalArgumentException("Expected ',' or '}' at position $index")
                }
            }
            return map
        }

        private fun parseArray(): List<Any?> {
            match('[')
            skipWhitespace()
            val list = mutableListOf<Any?>()
            if (hasMore() && currentChar() == ']') {
                index++
                return list
            }
            while (hasMore()) {
                val value = parseValue()
                list.add(value)
                skipWhitespace()
                if (hasMore() && currentChar() == ',') {
                    index++
                    continue
                } else if (hasMore() && currentChar() == ']') {
                    index++
                    break
                } else {
                    throw IllegalArgumentException("Expected ',' or ']' at position $index")
                }
            }
            return list
        }

        private fun parseString(): String {
            match('"')
            val sb = StringBuilder()
            while (hasMore()) {
                val c = src[index++]
                if (c == '"') {
                    return sb.toString()
                }
                if (c == '\\') {
                    if (!hasMore()) throw IllegalArgumentException("Unterminated escape sequence at $index")
                    when (val esc = src[index++]) {
                        '"' -> sb.append('"')
                        '\\' -> sb.append('\\')
                        '/' -> sb.append('/')
                        'b' -> sb.append('\b')
                        'f' -> sb.append('\u000C')
                        'n' -> sb.append('\n')
                        'r' -> sb.append('\r')
                        't' -> sb.append('\t')
                        'u' -> {
                            if (index + 4 > src.length) throw IllegalArgumentException("Invalid unicode escape at $index")
                            val hex = src.substring(index, index + 4)
                            index += 4
                            sb.append(hex.toInt(16).toChar())
                        }
                        else -> sb.append(esc)
                    }
                } else {
                    sb.append(c)
                }
            }
            throw IllegalArgumentException("Unterminated string")
        }

        private fun parseNumber(): Number {
            val start = index
            if (src[index] == '-') index++
            while (hasMore() && src[index].isDigit()) index++
            var isFloating = false
            if (hasMore() && src[index] == '.') {
                isFloating = true
                index++
                while (hasMore() && src[index].isDigit()) index++
            }
            if (hasMore() && (src[index] == 'e' || src[index] == 'E')) {
                isFloating = true
                index++
                if (hasMore() && (src[index] == '+' || src[index] == '-')) index++
                while (hasMore() && src[index].isDigit()) index++
            }
            val numStr = src.substring(start, index)
            return if (isFloating) {
                numStr.toDouble()
            } else {
                numStr.toLongOrNull() ?: numStr.toDouble()
            }
        }

        private fun parseBoolean(): Boolean {
            if (src.startsWith("true", index)) {
                index += 4
                return true
            }
            if (src.startsWith("false", index)) {
                index += 5
                return false
            }
            throw IllegalArgumentException("Expected boolean at position $index")
        }

        private fun parseNull(): Any? {
            if (src.startsWith("null", index)) {
                index += 4
                return null
            }
            throw IllegalArgumentException("Expected null at position $index")
        }

        private fun match(expected: Char) {
            if (!hasMore() || src[index] != expected) {
                val actual = if (hasMore()) src[index].toString() else "EOF"
                throw IllegalArgumentException("Expected '$expected' at $index, but got '$actual'")
            }
            index++
        }
    }
}

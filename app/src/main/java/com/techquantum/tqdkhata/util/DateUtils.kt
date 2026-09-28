package com.techquantum.tqdkhata.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DateUtils {

    private val dateTimeFormat = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
    private val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    private val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())

    fun formatDateTime(timestamp: Long?): String {
        if (timestamp == null || timestamp == 0L) return "Not set"
        return dateTimeFormat.format(Date(timestamp))
    }

    fun formatDate(timestamp: Long?): String {
        if (timestamp == null || timestamp == 0L) return "Not set"
        return dateFormat.format(Date(timestamp))
    }

    fun formatRelativeTime(timestamp: Long?): String {
        if (timestamp == null || timestamp == 0L) return "Not scheduled"

        val now = Calendar.getInstance()
        val target = Calendar.getInstance().apply { timeInMillis = timestamp }

        val isToday = now.get(Calendar.YEAR) == target.get(Calendar.YEAR) &&
                now.get(Calendar.DAY_OF_YEAR) == target.get(Calendar.DAY_OF_YEAR)

        val isTomorrow = now.get(Calendar.YEAR) == target.get(Calendar.YEAR) &&
                now.get(Calendar.DAY_OF_YEAR) + 1 == target.get(Calendar.DAY_OF_YEAR)

        val isYesterday = now.get(Calendar.YEAR) == target.get(Calendar.YEAR) &&
                now.get(Calendar.DAY_OF_YEAR) - 1 == target.get(Calendar.DAY_OF_YEAR)

        return when {
            isToday -> "Today at " + timeFormat.format(Date(timestamp))
            isTomorrow -> "Tomorrow at " + timeFormat.format(Date(timestamp))
            isYesterday -> "Yesterday at " + timeFormat.format(Date(timestamp))
            else -> dateTimeFormat.format(Date(timestamp))
        }
    }
}

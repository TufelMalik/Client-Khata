package com.techquantum.tqdkhata.utils.helpers

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DateUtils {

    private val displayDateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    private val displayTimeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
    private val displayDateTimeFormat = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())

    fun formatDate(timestamp: Long?): String {
        if (timestamp == null || timestamp == 0L) return "N/A"
        return displayDateFormat.format(Date(timestamp))
    }

    fun formatTime(timestamp: Long?): String {
        if (timestamp == null || timestamp == 0L) return ""
        return displayTimeFormat.format(Date(timestamp))
    }

    fun formatDateTime(timestamp: Long?): String {
        if (timestamp == null || timestamp == 0L) return "N/A"
        return displayDateTimeFormat.format(Date(timestamp))
    }

    fun isToday(timestamp: Long?): Boolean {
        if (timestamp == null) return false
        val cal1 = java.util.Calendar.getInstance()
        cal1.timeInMillis = timestamp
        val cal2 = java.util.Calendar.getInstance()
        return cal1.get(java.util.Calendar.YEAR) == cal2.get(java.util.Calendar.YEAR) &&
                cal1.get(java.util.Calendar.DAY_OF_YEAR) == cal2.get(java.util.Calendar.DAY_OF_YEAR)
    }

    fun isTomorrow(timestamp: Long?): Boolean {
        if (timestamp == null) return false
        val cal1 = java.util.Calendar.getInstance()
        cal1.timeInMillis = timestamp
        val cal2 = java.util.Calendar.getInstance()
        cal2.add(java.util.Calendar.DAY_OF_YEAR, 1)
        return cal1.get(java.util.Calendar.YEAR) == cal2.get(java.util.Calendar.YEAR) &&
                cal1.get(java.util.Calendar.DAY_OF_YEAR) == cal2.get(java.util.Calendar.DAY_OF_YEAR)
    }

    fun formatRelativeTime(timestamp: Long?): String {
        if (timestamp == null || timestamp == 0L) return "Not scheduled"

        val now = java.util.Calendar.getInstance()
        val target = java.util.Calendar.getInstance().apply { timeInMillis = timestamp }

        val isToday = now.get(java.util.Calendar.YEAR) == target.get(java.util.Calendar.YEAR) &&
                now.get(java.util.Calendar.DAY_OF_YEAR) == target.get(java.util.Calendar.DAY_OF_YEAR)

        val isTomorrow = now.get(java.util.Calendar.YEAR) == target.get(java.util.Calendar.YEAR) &&
                now.get(java.util.Calendar.DAY_OF_YEAR) + 1 == target.get(java.util.Calendar.DAY_OF_YEAR)

        val isYesterday = now.get(java.util.Calendar.YEAR) == target.get(java.util.Calendar.YEAR) &&
                now.get(java.util.Calendar.DAY_OF_YEAR) - 1 == target.get(java.util.Calendar.DAY_OF_YEAR)

        return when {
            isToday -> "Today at " + displayTimeFormat.format(Date(timestamp))
            isTomorrow -> "Tomorrow at " + displayTimeFormat.format(Date(timestamp))
            isYesterday -> "Yesterday at " + displayTimeFormat.format(Date(timestamp))
            else -> displayDateTimeFormat.format(Date(timestamp))
        }
    }

    fun isPast(timestamp: Long?): Boolean {
        if (timestamp == null) return false
        return timestamp < System.currentTimeMillis()
    }

    fun getEndOfTodayMillis(): Long {
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, 23)
        cal.set(Calendar.MINUTE, 59)
        cal.set(Calendar.SECOND, 59)
        cal.set(Calendar.MILLISECOND, 999)
        return cal.timeInMillis
    }

    fun getStartOfTodayMillis(): Long {
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }
}

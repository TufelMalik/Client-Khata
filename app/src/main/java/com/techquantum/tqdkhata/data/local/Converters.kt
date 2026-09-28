package com.techquantum.tqdkhata.data.local

import androidx.room.TypeConverter
import com.techquantum.tqdkhata.data.model.Priority
import com.techquantum.tqdkhata.data.model.ProjectStatus

class Converters {
    @TypeConverter
    fun fromProjectStatus(status: ProjectStatus?): String? = status?.name

    @TypeConverter
    fun toProjectStatus(value: String?): ProjectStatus? =
        value?.let { ProjectStatus.fromString(it) }

    @TypeConverter
    fun fromPriority(priority: Priority?): String? = priority?.name

    @TypeConverter
    fun toPriority(value: String?): Priority? =
        value?.let { Priority.fromString(it) }
}

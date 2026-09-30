package com.techquantum.tqdkhata.model.data.domain

import com.techquantum.tqdkhata.domain.model.Reminder
import com.techquantum.tqdkhata.model.data.local.ReminderEntity

fun ReminderEntity.toDomain(): Reminder = Reminder(
    id = id,
    clientId = clientId,
    title = title,
    notes = notes,
    reminderTimestamp = reminderTimestamp,
    isCompleted = isCompleted,
    createdAt = createdAt
)

fun Reminder.toEntity(): ReminderEntity = ReminderEntity(
    id = id,
    clientId = clientId,
    title = title,
    notes = notes,
    reminderTimestamp = reminderTimestamp,
    isCompleted = isCompleted,
    createdAt = createdAt
)

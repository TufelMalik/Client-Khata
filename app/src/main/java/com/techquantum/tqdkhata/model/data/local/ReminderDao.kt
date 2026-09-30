package com.techquantum.tqdkhata.model.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ReminderDao {

    @Query("SELECT * FROM reminders WHERE clientId = :clientId ORDER BY createdAt DESC")
    fun getRemindersForClient(clientId: Long): Flow<List<ReminderEntity>>

    @Query("""
        SELECT r.id, r.clientId, c.name AS clientName, c.phone AS clientPhone,
               r.title, r.notes, r.reminderTimestamp, r.isCompleted, r.createdAt
        FROM reminders r
        INNER JOIN clients c ON r.clientId = c.id
        ORDER BY r.isCompleted ASC, r.reminderTimestamp ASC, r.createdAt DESC
    """)
    fun getAllRemindersWithClient(): Flow<List<ReminderWithClient>>

    @Query("""
        SELECT r.id, r.clientId, c.name AS clientName, c.phone AS clientPhone,
               r.title, r.notes, r.reminderTimestamp, r.isCompleted, r.createdAt
        FROM reminders r
        INNER JOIN clients c ON r.clientId = c.id
        WHERE r.isCompleted = 0
        ORDER BY r.reminderTimestamp ASC, r.createdAt DESC
    """)
    fun getPendingRemindersWithClient(): Flow<List<ReminderWithClient>>

    @Query("SELECT * FROM reminders ORDER BY id ASC")
    suspend fun getAllRemindersList(): List<ReminderEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReminder(reminder: ReminderEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReminders(reminders: List<ReminderEntity>): List<Long>

    @Update
    suspend fun updateReminder(reminder: ReminderEntity)

    @Delete
    suspend fun deleteReminder(reminder: ReminderEntity)

    @Query("DELETE FROM reminders")
    suspend fun deleteAllReminders()

    @Query("""
        SELECT r.id, r.clientId, c.name AS clientName, c.phone AS clientPhone,
               r.title, r.notes, r.reminderTimestamp, r.isCompleted, r.createdAt
        FROM reminders r
        INNER JOIN clients c ON r.clientId = c.id
        WHERE r.id = :id
        LIMIT 1
    """)
    suspend fun getReminderWithClientById(id: Long): ReminderWithClient?

    @Query("""
        SELECT r.id, r.clientId, c.name AS clientName, c.phone AS clientPhone,
               r.title, r.notes, r.reminderTimestamp, r.isCompleted, r.createdAt
        FROM reminders r
        INNER JOIN clients c ON r.clientId = c.id
        WHERE r.isCompleted = 0 AND r.reminderTimestamp IS NOT NULL AND r.reminderTimestamp > :currentTime
        ORDER BY r.reminderTimestamp ASC
    """)
    suspend fun getActiveRemindersAfter(currentTime: Long): List<ReminderWithClient>

    @Query("SELECT * FROM reminders WHERE clientId = :clientId")
    suspend fun getRemindersForClientList(clientId: Long): List<ReminderEntity>

    @Query("UPDATE reminders SET isCompleted = :isCompleted WHERE id = :id")
    suspend fun setCompletionStatus(id: Long, isCompleted: Boolean)

    @Query("""
        SELECT r.id, r.clientId, c.name AS clientName, c.phone AS clientPhone,
               r.title, r.notes, r.reminderTimestamp, r.isCompleted, r.createdAt
        FROM reminders r
        INNER JOIN clients c ON r.clientId = c.id
        WHERE r.isCompleted = 0 
          AND r.reminderTimestamp IS NOT NULL 
          AND r.reminderTimestamp <= :endOfDay
        ORDER BY r.reminderTimestamp ASC
    """)
    fun getTodaysRemindersWithClient(endOfDay: Long): Flow<List<ReminderWithClient>>

    @Query("""
        SELECT COUNT(*) FROM reminders 
        WHERE isCompleted = 0 
          AND reminderTimestamp IS NOT NULL 
          AND reminderTimestamp <= :endOfDay
    """)
    fun getTodaysFollowUpCount(endOfDay: Long): Flow<Int>
}

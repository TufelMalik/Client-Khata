package com.techquantum.tqdkhata.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.techquantum.tqdkhata.data.model.ReminderEntity
import com.techquantum.tqdkhata.data.model.ReminderWithClient
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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReminder(reminder: ReminderEntity): Long

    @Update
    suspend fun updateReminder(reminder: ReminderEntity)

    @Delete
    suspend fun deleteReminder(reminder: ReminderEntity)

    @Query("UPDATE reminders SET isCompleted = :isCompleted WHERE id = :id")
    suspend fun setCompletionStatus(id: Long, isCompleted: Boolean)
}

package com.techquantum.tqdkhata.repository

import android.content.Context
import androidx.room.withTransaction
import com.techquantum.tqdkhata.domain.repository.ClientRepository
import com.techquantum.tqdkhata.model.data.local.AppDatabase
import com.techquantum.tqdkhata.model.data.local.ClientDao
import com.techquantum.tqdkhata.model.data.local.ClientResourceDao
import com.techquantum.tqdkhata.model.data.local.ReminderDao
import com.techquantum.tqdkhata.model.data.local.ClientEntity
import com.techquantum.tqdkhata.model.data.local.ClientResourceEntity
import com.techquantum.tqdkhata.model.data.local.ReminderEntity
import com.techquantum.tqdkhata.model.data.local.ReminderWithClient
import com.techquantum.tqdkhata.model.enums.ProjectStatus
import com.techquantum.tqdkhata.model.response.BackupData
import com.techquantum.tqdkhata.model.response.ImportResult
import com.techquantum.tqdkhata.utils.helpers.JsonBackupUtils
import com.techquantum.tqdkhata.utils.helpers.MediaUtils
import com.techquantum.tqdkhata.utils.helpers.ReminderAlarmScheduler
import com.techquantum.tqdkhata.utils.helpers.DateUtils
import androidx.glance.appwidget.updateAll
import com.techquantum.tqdkhata.widget.TQDKhataWidget
import kotlinx.coroutines.flow.Flow
import java.io.File

class ClientRepositoryImpl(
    private val clientDao: ClientDao,
    private val reminderDao: ReminderDao,
    private val clientResourceDao: ClientResourceDao,
    private val database: AppDatabase,
    private val context: Context? = null
) : ClientRepository {

    private suspend fun updateWidget() {
        context?.let { ctx ->
            try {
                TQDKhataWidget().updateAll(ctx)
            } catch (_: Throwable) {}
        }
    }

    override fun filterClients(
        query: String?,
        city: String?,
        status: ProjectStatus?
    ): Flow<List<ClientEntity>> {
        val queryParam = if (query.isNullOrBlank()) null else query.trim()
        val cityParam = if (city.isNullOrBlank() || city == "All") null else city.trim()
        val statusParam = status?.name
        return clientDao.filterClients(queryParam, cityParam, statusParam)
    }

    override fun getAllClients(): Flow<List<ClientEntity>> = clientDao.getAllClients()

    override fun getClientById(id: Long): Flow<ClientEntity?> = clientDao.getClientById(id)

    override fun getAllDistinctCities(): Flow<List<String>> = clientDao.getAllDistinctCities()

    override fun getTotalClientCount(): Flow<Int> = clientDao.getTotalClientCount()

    override fun getCountByStatus(status: ProjectStatus): Flow<Int> =
        clientDao.getCountByStatus(status.name)

    override suspend fun saveClient(client: ClientEntity): Long {
        val id = if (client.id == 0L) {
            clientDao.insertClient(client)
        } else {
            clientDao.updateClient(client.copy(updatedAt = System.currentTimeMillis()))
            client.id
        }
        updateWidget()
        return id
    }

    override suspend fun deleteClient(client: ClientEntity) {
        context?.let { ctx ->
            val reminders = reminderDao.getRemindersForClientList(client.id)
            reminders.forEach { rem ->
                ReminderAlarmScheduler.cancelReminder(ctx, rem.id)
            }
        }
        try {
            val resources = clientResourceDao.getResourcesForClientList(client.id)
            resources.forEach { res ->
                val file = File(res.filePath)
                if (file.exists()) file.delete()
            }
        } catch (_: Exception) {}
        clientDao.deleteClient(client)
        updateWidget()
    }

    override suspend fun deleteClientById(id: Long) {
        context?.let { ctx ->
            val reminders = reminderDao.getRemindersForClientList(id)
            reminders.forEach { rem ->
                ReminderAlarmScheduler.cancelReminder(ctx, rem.id)
            }
        }
        try {
            val resources = clientResourceDao.getResourcesForClientList(id)
            resources.forEach { res ->
                val file = File(res.filePath)
                if (file.exists()) file.delete()
            }
        } catch (_: Exception) {}
        clientDao.deleteClientById(id)
        updateWidget()
    }

    // Reminders
    override fun getRemindersForClient(clientId: Long): Flow<List<ReminderEntity>> =
        reminderDao.getRemindersForClient(clientId)

    override fun getAllRemindersWithClient(): Flow<List<ReminderWithClient>> =
        reminderDao.getAllRemindersWithClient()

    override fun getPendingRemindersWithClient(): Flow<List<ReminderWithClient>> =
        reminderDao.getPendingRemindersWithClient()

    override fun getTodaysRemindersWithClient(): Flow<List<ReminderWithClient>> {
        val endOfDay = DateUtils.getEndOfTodayMillis()
        return reminderDao.getTodaysRemindersWithClient(endOfDay)
    }

    override fun getTodaysFollowUpCount(): Flow<Int> {
        val endOfDay = DateUtils.getEndOfTodayMillis()
        return reminderDao.getTodaysFollowUpCount(endOfDay)
    }

    override suspend fun saveReminder(reminder: ReminderEntity): Long {
        val savedId = if (reminder.id == 0L) {
            reminderDao.insertReminder(reminder)
        } else {
            reminderDao.updateReminder(reminder)
            reminder.id
        }

        context?.let { ctx ->
            if (reminder.reminderTimestamp != null &&
                reminder.reminderTimestamp > System.currentTimeMillis() &&
                !reminder.isCompleted
            ) {
                val client = clientDao.getClientByIdDirect(reminder.clientId)
                ReminderAlarmScheduler.scheduleReminder(
                    context = ctx,
                    reminderId = savedId,
                    clientId = reminder.clientId,
                    clientName = client?.name ?: "",
                    clientPhone = client?.phone ?: "",
                    title = reminder.title,
                    notes = reminder.notes,
                    triggerTime = reminder.reminderTimestamp
                )
            } else {
                ReminderAlarmScheduler.cancelReminder(ctx, savedId)
            }
        }

        updateWidget()
        return savedId
    }

    override suspend fun deleteReminder(reminder: ReminderEntity) {
        context?.let { ctx ->
            ReminderAlarmScheduler.cancelReminder(ctx, reminder.id)
        }
        reminderDao.deleteReminder(reminder)
        updateWidget()
    }

    override suspend fun setReminderCompletion(id: Long, completed: Boolean) {
        reminderDao.setCompletionStatus(id, completed)
        context?.let { ctx ->
            if (completed) {
                ReminderAlarmScheduler.cancelReminder(ctx, id)
            } else {
                val rem = reminderDao.getReminderWithClientById(id)
                if (rem != null && rem.reminderTimestamp != null && rem.reminderTimestamp > System.currentTimeMillis()) {
                    ReminderAlarmScheduler.scheduleReminder(
                        context = ctx,
                        reminderId = rem.id,
                        clientId = rem.clientId,
                        clientName = rem.clientName,
                        clientPhone = rem.clientPhone,
                        title = rem.title,
                        notes = rem.notes,
                        triggerTime = rem.reminderTimestamp
                    )
                }
            }
        }
        updateWidget()
    }

    // Media & Resources
    override fun getResourcesForClient(clientId: Long): Flow<List<ClientResourceEntity>> =
        clientResourceDao.getResourcesForClient(clientId)

    override suspend fun saveResource(resource: ClientResourceEntity): Long =
        clientResourceDao.insertResource(resource)

    override suspend fun deleteResource(resource: ClientResourceEntity) {
        clientResourceDao.deleteResource(resource)
        try {
            val file = File(resource.filePath)
            if (file.exists()) {
                file.delete()
            }
        } catch (_: Exception) {}
    }

    override suspend fun cleanupAllVideoResources() {
        try {
            val videoResources = clientResourceDao.getAllVideoResources()
            videoResources.forEach { res ->
                try {
                    val file = File(res.filePath)
                    if (file.exists()) file.delete()
                } catch (_: Exception) {}
            }
            clientResourceDao.deleteAllVideoResources()
        } catch (_: Exception) {}
        context?.let { ctx ->
            MediaUtils.deleteAllCapturedVideos(ctx)
        }
    }

    // Export & Import
    override suspend fun getAllClientsList(): List<ClientEntity> = clientDao.getAllClientsList()

    override suspend fun getAllRemindersList(): List<ReminderEntity> = reminderDao.getAllRemindersList()

    override suspend fun exportDataToJson(): String {
        val clients = clientDao.getAllClientsList()
        val reminders = reminderDao.getAllRemindersList()
        return JsonBackupUtils.createBackupJson(clients, reminders)
    }

    override suspend fun importBackupData(backupData: BackupData): ImportResult {
        val newlyScheduledReminders = mutableListOf<Triple<Long, Long, ReminderEntity>>()
        val result = database.withTransaction {
            val clientIdMap = mutableMapOf<Long, Long>()
            var clientsImported = 0
            var remindersImported = 0

            for (client in backupData.clients) {
                val oldId = client.id
                val newId = clientDao.insertClient(client.copy(id = 0L))
                if (oldId != 0L) {
                    clientIdMap[oldId] = newId
                }
                clientsImported++
            }

            for (reminder in backupData.reminders) {
                val mappedClientId = clientIdMap[reminder.clientId]
                val targetClientId = mappedClientId ?: if (clientDao.clientExists(reminder.clientId) > 0) reminder.clientId else null

                if (targetClientId != null && targetClientId > 0L) {
                    val remToInsert = reminder.copy(
                        id = 0L,
                        clientId = targetClientId
                    )
                    val newReminderId = reminderDao.insertReminder(remToInsert)
                    remindersImported++

                    if (reminder.reminderTimestamp != null &&
                        reminder.reminderTimestamp > System.currentTimeMillis() &&
                        !reminder.isCompleted
                    ) {
                        newlyScheduledReminders.add(Triple(newReminderId, targetClientId, remToInsert))
                    }
                }
            }

            ImportResult(clientsImported, remindersImported)
        }

        context?.let { ctx ->
            for ((remId, cId, rem) in newlyScheduledReminders) {
                val client = clientDao.getClientByIdDirect(cId)
                val trigger = rem.reminderTimestamp ?: continue
                ReminderAlarmScheduler.scheduleReminder(
                    context = ctx,
                    reminderId = remId,
                    clientId = cId,
                    clientName = client?.name ?: "",
                    clientPhone = client?.phone ?: "",
                    title = rem.title,
                    notes = rem.notes,
                    triggerTime = trigger
                )
            }
        }

        updateWidget()
        return result
    }

    override suspend fun importDataFromJson(jsonData: String): ImportResult {
        val backupData = JsonBackupUtils.parseBackupJson(jsonData)
        return importBackupData(backupData)
    }
}

package com.techquantum.tqdkhata.data.repository

import com.techquantum.tqdkhata.data.local.ClientDao
import com.techquantum.tqdkhata.data.local.ReminderDao
import com.techquantum.tqdkhata.data.model.ClientEntity
import com.techquantum.tqdkhata.data.model.ProjectStatus
import com.techquantum.tqdkhata.data.model.ReminderEntity
import com.techquantum.tqdkhata.data.model.ReminderWithClient
import kotlinx.coroutines.flow.Flow

class ClientRepository(
    private val clientDao: ClientDao,
    private val reminderDao: ReminderDao
) {
    fun filterClients(
        query: String?,
        city: String?,
        status: ProjectStatus?
    ): Flow<List<ClientEntity>> {
        val queryParam = if (query.isNullOrBlank()) null else query.trim()
        val cityParam = if (city.isNullOrBlank() || city == "All") null else city.trim()
        val statusParam = status?.name
        return clientDao.filterClients(queryParam, cityParam, statusParam)
    }

    fun getAllClients(): Flow<List<ClientEntity>> = clientDao.getAllClients()

    fun getClientById(id: Long): Flow<ClientEntity?> = clientDao.getClientById(id)

    fun getAllDistinctCities(): Flow<List<String>> = clientDao.getAllDistinctCities()

    fun getTotalClientCount(): Flow<Int> = clientDao.getTotalClientCount()

    fun getCountByStatus(status: ProjectStatus): Flow<Int> =
        clientDao.getCountByStatus(status.name)

    suspend fun saveClient(client: ClientEntity): Long {
        return if (client.id == 0L) {
            clientDao.insertClient(client)
        } else {
            clientDao.updateClient(client.copy(updatedAt = System.currentTimeMillis()))
            client.id
        }
    }

    suspend fun deleteClient(client: ClientEntity) = clientDao.deleteClient(client)

    suspend fun deleteClientById(id: Long) = clientDao.deleteClientById(id)

    // Reminders
    fun getRemindersForClient(clientId: Long): Flow<List<ReminderEntity>> =
        reminderDao.getRemindersForClient(clientId)

    fun getAllRemindersWithClient(): Flow<List<ReminderWithClient>> =
        reminderDao.getAllRemindersWithClient()

    fun getPendingRemindersWithClient(): Flow<List<ReminderWithClient>> =
        reminderDao.getPendingRemindersWithClient()

    suspend fun saveReminder(reminder: ReminderEntity): Long {
        return if (reminder.id == 0L) {
            reminderDao.insertReminder(reminder)
        } else {
            reminderDao.updateReminder(reminder)
            reminder.id
        }
    }

    suspend fun deleteReminder(reminder: ReminderEntity) = reminderDao.deleteReminder(reminder)

    suspend fun setReminderCompletion(id: Long, completed: Boolean) =
        reminderDao.setCompletionStatus(id, completed)
}

package com.techquantum.tqdkhata.model.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ClientDao {

    @Query("SELECT * FROM clients ORDER BY updatedAt DESC")
    fun getAllClients(): Flow<List<ClientEntity>>

    @Query("""
        SELECT * FROM clients 
        WHERE (:query IS NULL OR :query = '' OR name LIKE '%' || :query || '%' 
               OR phone LIKE '%' || :query || '%' 
               OR requirements LIKE '%' || :query || '%'
               OR businessName LIKE '%' || :query || '%')
          AND (:city IS NULL OR :city = '' OR city = :city)
          AND (:status IS NULL OR status = :status)
        ORDER BY updatedAt DESC
    """)
    fun filterClients(
        query: String?,
        city: String?,
        status: String?
    ): Flow<List<ClientEntity>>

    @Query("SELECT * FROM clients WHERE id = :id LIMIT 1")
    fun getClientById(id: Long): Flow<ClientEntity?>

    @Query("SELECT DISTINCT city FROM clients WHERE city IS NOT NULL AND city != '' ORDER BY city ASC")
    fun getAllDistinctCities(): Flow<List<String>>

    @Query("SELECT COUNT(*) FROM clients")
    fun getTotalClientCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM clients WHERE status = :status")
    fun getCountByStatus(status: String): Flow<Int>

    @Query("SELECT * FROM clients ORDER BY id ASC")
    suspend fun getAllClientsList(): List<ClientEntity>

    @Query("SELECT COUNT(*) FROM clients WHERE id = :id")
    suspend fun clientExists(id: Long): Int

    @Query("SELECT * FROM clients WHERE id = :id LIMIT 1")
    suspend fun getClientByIdDirect(id: Long): ClientEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClient(client: ClientEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClients(clients: List<ClientEntity>): List<Long>

    @Update
    suspend fun updateClient(client: ClientEntity)

    @Delete
    suspend fun deleteClient(client: ClientEntity)

    @Query("DELETE FROM clients WHERE id = :id")
    suspend fun deleteClientById(id: Long)

    @Query("DELETE FROM clients")
    suspend fun deleteAllClients()
}

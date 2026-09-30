package com.techquantum.tqdkhata.model.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ClientResourceDao {

    @Query("SELECT * FROM client_resources WHERE clientId = :clientId ORDER BY createdAt DESC")
    fun getResourcesForClient(clientId: Long): Flow<List<ClientResourceEntity>>

    @Query("SELECT * FROM client_resources WHERE clientId = :clientId ORDER BY createdAt DESC")
    suspend fun getResourcesForClientList(clientId: Long): List<ClientResourceEntity>

    @Query("SELECT * FROM client_resources ORDER BY id ASC")
    suspend fun getAllResourcesList(): List<ClientResourceEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResource(resource: ClientResourceEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResources(resources: List<ClientResourceEntity>): List<Long>

    @Delete
    suspend fun deleteResource(resource: ClientResourceEntity)

    @Query("DELETE FROM client_resources WHERE id = :id")
    suspend fun deleteResourceById(id: Long)

    @Query("DELETE FROM client_resources")
    suspend fun deleteAllResources()

    @Query("SELECT * FROM client_resources WHERE resourceType = 'VIDEO'")
    suspend fun getAllVideoResources(): List<ClientResourceEntity>

    @Query("DELETE FROM client_resources WHERE resourceType = 'VIDEO'")
    suspend fun deleteAllVideoResources()
}

package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.ServiceRequestEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ServiceRequestDao {
    @Query("SELECT * FROM service_requests ORDER BY createdAt DESC")
    fun getAllRequests(): Flow<List<ServiceRequestEntity>>

    @Query("SELECT * FROM service_requests WHERE id = :id")
    fun getRequestById(id: String): Flow<ServiceRequestEntity?>

    @Query("SELECT * FROM service_requests WHERE status != 'COMPLETED' AND status != 'CANCELLED' ORDER BY createdAt DESC LIMIT 1")
    fun getActiveRequest(): Flow<ServiceRequestEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRequest(request: ServiceRequestEntity)

    @Update
    suspend fun updateRequest(request: ServiceRequestEntity)

    @Query("UPDATE service_requests SET status = :status WHERE id = :id")
    suspend fun updateStatus(id: String, status: String)

    @Query("DELETE FROM service_requests WHERE id = :id")
    suspend fun deleteById(id: String)
}

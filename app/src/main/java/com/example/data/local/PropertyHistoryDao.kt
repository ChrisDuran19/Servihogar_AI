package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.PropertyHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PropertyHistoryDao {
    @Query("SELECT * FROM property_history ORDER BY id DESC")
    fun getAllHistory(): Flow<List<PropertyHistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(history: PropertyHistoryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(historyList: List<PropertyHistoryEntity>)
}

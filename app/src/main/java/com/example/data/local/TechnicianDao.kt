package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.TechnicianEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TechnicianDao {
    @Query("SELECT * FROM technicians")
    fun getAllTechnicians(): Flow<List<TechnicianEntity>>

    @Query("SELECT * FROM technicians WHERE category = :category")
    fun getTechniciansByCategory(category: String): Flow<List<TechnicianEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(techs: List<TechnicianEntity>)
}

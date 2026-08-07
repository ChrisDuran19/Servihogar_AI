package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.PropertyHistoryEntity
import com.example.data.model.ServiceRequestEntity
import com.example.data.model.TechnicianEntity

@Database(
    entities = [
        ServiceRequestEntity::class,
        PropertyHistoryEntity::class,
        TechnicianEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun serviceRequestDao(): ServiceRequestDao
    abstract fun propertyHistoryDao(): PropertyHistoryDao
    abstract fun technicianDao(): TechnicianDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "servihogar_ai.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

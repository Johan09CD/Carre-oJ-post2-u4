package com.universidad.taskmanager.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// 1. Incrementamos la versión a 2 [cite: 232]
@Database(entities = [TaskEntity::class], version = 2, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "taskmanager.db"
                )
                    .addMigrations(MIGRATION_1_2) // 2. Registramos la migración aquí [cite: 242]
                    .build()
                    .also { INSTANCE = it }
            }
    }
}
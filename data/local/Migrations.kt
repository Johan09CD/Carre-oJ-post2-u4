package com.universidad.taskmanager.data.local

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

// Definimos la migración de la versión 1 a la 2 [cite: 218, 219]
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Agregamos la columna priority con un valor por defecto de 1 [cite: 221, 223]
        database.execSQL(
            "ALTER TABLE tasks ADD COLUMN priority INTEGER NOT NULL DEFAULT 1"
        )
    }
}
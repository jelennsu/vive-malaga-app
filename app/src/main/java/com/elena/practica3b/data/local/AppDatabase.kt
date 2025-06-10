package com.elena.practica3b.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.elena.practica3b.data.local.dao.HistorialDao
import com.elena.practica3b.data.local.entity.Historial

@Database(entities = [Historial::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun historialDao(): HistorialDao
}


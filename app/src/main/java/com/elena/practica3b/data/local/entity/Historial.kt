package com.elena.practica3b.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "historial")
data class Historial(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val lugarId: String, // ID del lugar en Firebase
    val nombre: String,
    val timestamp: Long = System.currentTimeMillis(),
    val usuarioId: String
)

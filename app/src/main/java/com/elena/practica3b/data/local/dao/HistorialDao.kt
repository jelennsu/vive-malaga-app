package com.elena.practica3b.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.elena.practica3b.data.local.entity.Historial
import kotlinx.coroutines.flow.Flow

@Dao
interface HistorialDao {

    @Query("SELECT * FROM historial WHERE usuarioId = :usuarioId ORDER BY timestamp DESC")
    fun obtenerHistorialPorUsuario(usuarioId: String): Flow<List<Historial>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(historial: Historial)

    @Query("DELETE FROM historial WHERE usuarioId = :usuarioId")
    suspend fun borrarHistorialDeUsuario(usuarioId: String)
}




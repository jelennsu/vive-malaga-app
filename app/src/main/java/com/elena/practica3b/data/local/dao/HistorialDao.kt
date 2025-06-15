package com.elena.practica3b.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.elena.practica3b.data.local.entity.Historial
import kotlinx.coroutines.flow.Flow

/**
 * DAO para gestionar el historial de lugares visitados por el usuario.
 * Proporciona operaciones para insertar, consultar y eliminar registros.
 */
@Dao
interface HistorialDao {

    /**
     * Obtiene el historial de un usuario concreto, ordenado por fecha descendente.
     * Devuelve un Flow para actualizaciones reactivas en tiempo real.
     */
    @Query("SELECT * FROM historial WHERE usuarioId = :usuarioId ORDER BY timestamp DESC")
    fun obtenerHistorialPorUsuario(usuarioId: String): Flow<List<Historial>>

    /**
     * Inserta un nuevo registro en el historial.
     * Si ya existe uno con el mismo ID, lo reemplaza.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(historial: Historial)

    /**
     * Elimina todos los registros del historial pertenecientes al usuario indicado.
     */
    @Query("DELETE FROM historial WHERE usuarioId = :usuarioId")
    suspend fun borrarHistorialDeUsuario(usuarioId: String)
}





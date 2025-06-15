package com.elena.practica3b.data.repository

import com.elena.practica3b.data.local.dao.HistorialDao
import com.elena.practica3b.data.local.entity.Historial
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Repositorio que actúa como intermediario entre el ViewModel y la capa de acceso a datos local (Room).
 * Gestiona las operaciones relacionadas con el historial de lugares visitados.
 */
class HistorialRepository @Inject constructor(
    private val historialDao: HistorialDao
) {

    /**
     * Devuelve un flujo reactivo del historial del usuario especificado.
     */
    fun obtenerHistorial(usuarioId: String): Flow<List<Historial>> {
        return historialDao.obtenerHistorialPorUsuario(usuarioId)
    }

    /**
     * Inserta un nuevo registro de historial en la base de datos local.
     */
    suspend fun insertar(historial: Historial) {
        historialDao.insertar(historial)
    }

    /**
     * Elimina todos los registros del historial del usuario especificado.
     */
    suspend fun borrarTodo(usuarioId: String) {
        historialDao.borrarHistorialDeUsuario(usuarioId)
    }
}



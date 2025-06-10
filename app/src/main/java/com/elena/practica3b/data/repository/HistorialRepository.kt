package com.elena.practica3b.data.repository

import com.elena.practica3b.data.local.dao.HistorialDao
import com.elena.practica3b.data.local.entity.Historial
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HistorialRepository @Inject constructor(
    private val historialDao: HistorialDao
) {
    fun obtenerHistorial(usuarioId: String): Flow<List<Historial>> {
        return historialDao.obtenerHistorialPorUsuario(usuarioId)
    }

    suspend fun insertar(historial: Historial) {
        historialDao.insertar(historial)
    }

    suspend fun borrarTodo(usuarioId: String) {
        historialDao.borrarHistorialDeUsuario(usuarioId)
    }
}




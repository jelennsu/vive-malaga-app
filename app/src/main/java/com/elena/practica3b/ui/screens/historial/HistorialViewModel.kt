package com.elena.practica3b.ui.screens.historial

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elena.practica3b.data.local.entity.Historial
import com.elena.practica3b.data.repository.HistorialRepository
import com.elena.practica3b.ui.screens.lugar.Lugar
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/*
 * ViewModel para gestionar el historial de lugares visitados por el usuario.
 * Proporciona un StateFlow con el historial obtenido desde el repositorio local,
 * y funciones para añadir un lugar al historial y borrar todo el historial del usuario.
 * Maneja la asociación con el usuario autenticado vía FirebaseAuth.
 */

@HiltViewModel
class HistorialViewModel @Inject constructor(
    private val historialRepository: HistorialRepository,
    auth: FirebaseAuth
) : ViewModel() {

    private val usuarioId: String? = auth.currentUser?.uid

    val historial: StateFlow<List<Historial>> =
        if (usuarioId != null) {
            historialRepository
                .obtenerHistorial(usuarioId)
                .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
        } else {
            MutableStateFlow(emptyList())
        }

    fun guardarEnHistorial(lugar: Lugar) {
        usuarioId?.let { uid ->
            viewModelScope.launch {
                val historialItem = Historial(
                    lugarId = lugar.id,
                    nombre = lugar.nombre,
                    timestamp = System.currentTimeMillis(),
                    usuarioId = uid
                )
                historialRepository.insertar(historialItem)
            }
        }
    }

    fun borrarHistorial() {
        usuarioId?.let { uid ->
            viewModelScope.launch {
                historialRepository.borrarTodo(uid)
            }
        }
    }
}


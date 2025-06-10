package com.elena.practica3b.ui.screens.historial

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elena.practica3b.data.local.entity.Historial
import com.elena.practica3b.data.repository.HistorialRepository
import com.elena.practica3b.ui.screens.lugar.data.Lugar
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

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


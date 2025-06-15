package com.elena.practica3b.ui.screens.reservas

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.google.firebase.auth.FirebaseAuth

/**
 * ViewModel responsable de gestionar las reservas del usuario actual.
 *
 * Funcionalidades principales:
 * - Cargar las reservas asociadas al usuario autenticado desde Firestore.
 * - Obtener el nombre del lugar asociado a cada reserva para mostrar en la UI.
 * - Permitir cancelar reservas eliminándolas de Firestore y actualizando la lista local.
 *
 * Utiliza inyección de dependencias con Hilt para FirebaseFirestore y FirebaseAuth.
 * Mantiene el estado de las reservas en una lista observable para Jetpack Compose.
 */

@HiltViewModel
class ReservasViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    auth: FirebaseAuth
) : ViewModel() {

    private val _reservas = mutableStateListOf<Reserva>()
    val reservas: List<Reserva> get() = _reservas

    init {
        val usuarioId = auth.currentUser?.uid
        if (usuarioId != null) {
            cargarReservas(usuarioId)
        }
    }

    fun cargarReservas(usuarioId: String) {
        firestore.collection("reservas")
            .whereEqualTo("usuarioId", usuarioId)
            .get()
            .addOnSuccessListener { result ->
                _reservas.clear()

                for (document in result) {
                    val reserva = document.toObject(Reserva::class.java)

                    firestore.collection("lugares")
                        .document(reserva.lugarId)
                        .get()
                        .addOnSuccessListener { lugarDoc ->
                            reserva.nombreLugar = lugarDoc.getString("nombre") ?: "Lugar desconocido"
                            _reservas.add(reserva)
                        }
                        .addOnFailureListener {
                            reserva.nombreLugar = "Lugar desconocido"
                            _reservas.add(reserva)
                        }
                }
            }
            .addOnFailureListener { exception ->
                Log.e("ReservasViewModel", "Error cargando reservas", exception)
            }
    }


    fun cancelarReserva(reserva: Reserva) {
        firestore.collection("reservas")
            .document(reserva.id)
            .delete()
            .addOnSuccessListener {
                _reservas.remove(reserva)
            }
            .addOnFailureListener { e ->
                Log.e("ReservasViewModel", "Error al cancelar reserva", e)
            }
    }
}


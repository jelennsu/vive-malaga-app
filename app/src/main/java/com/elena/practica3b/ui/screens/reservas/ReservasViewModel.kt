package com.elena.practica3b.ui.screens.reservas

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.elena.practica3b.ui.screens.lugar.data.Reserva
import com.google.firebase.auth.FirebaseAuth

// ViewModel de la pantalla de la galería.
// Gestiona la lógica de negocio relacionada con las imágenes, incluyendo:
// 1. La subida de imágenes a Firebase Storage mediante `addImageToStorage`.
// 2. La inserción de las URL de las imágenes en Firestore con `addImageToDatabase`.
// 3. La obtención de las imágenes almacenadas en Firestore con `getImageFromDatabase`.
// 4. La eliminación de imágenes tanto de Firestore como de Firebase Storage a través de `deleteImageFromFirestoreAndStorage`.
// Además, mantiene el estado de las respuestas (éxito, carga, error) para cada operación y un indicador de carga durante la eliminación de imágenes.

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


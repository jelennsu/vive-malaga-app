package com.elena.practica3b.ui.screens.lugar

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elena.practica3b.data.repository.HistorialRepository
import com.elena.practica3b.ui.screens.lugar.data.Lugar
import com.elena.practica3b.ui.screens.lugar.data.Reserva
import com.elena.practica3b.ui.screens.lugar.data.Review
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class LugarViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _state = MutableStateFlow(LugarState())
    val state: StateFlow<LugarState> = _state.asStateFlow()

    private val _isSendingReview = MutableStateFlow(false)
    val isSendingReview: StateFlow<Boolean> = _isSendingReview.asStateFlow()


    fun loadLugar(id: String) {
        _state.update { it.copy(isLoading = true, errorMessage = null) }

        firestore.collection("lugares").document(id).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val lugar = document.toObject(Lugar::class.java)
                    if (lugar != null) {
                        val lugarConId = lugar.copy(id = document.id)
                        _state.update { it.copy(lugar = lugarConId, isLoading = false) }
                    } else {
                        _state.update { it.copy(errorMessage = "Error al convertir el documento", isLoading = false) }
                    }
                } else {
                    _state.update { it.copy(errorMessage = "Lugar no encontrado", isLoading = false) }
                }
            }
            .addOnFailureListener { e ->
                _state.update { it.copy(errorMessage = e.localizedMessage ?: "Error desconocido", isLoading = false) }
            }
    }

    suspend fun isFechaHoraDisponible(lugarId: String, fechaHora: Long): Boolean {
        val querySnapshot = firestore.collection("reservas")
            .whereEqualTo("lugarId", lugarId)
            .whereEqualTo("fechaHora", fechaHora)
            .get()
            .await()
        return querySnapshot.isEmpty
    }

    fun hacerReserva(
        lugarId: String,
        fechaHora: Long,
        cantidad: Int,
        onResult: (Boolean, String?) -> Unit
    ) {
        val usuarioId = auth.currentUser?.uid
        if (usuarioId == null) {
            onResult(false, "Usuario no autenticado")
            return
        }
        viewModelScope.launch {
            val disponible = isFechaHoraDisponible(lugarId, fechaHora)
            if (!disponible) {
                onResult(false, "Esa fecha y hora ya está reservada")
                return@launch
            }
            val reserva = Reserva(
                id = firestore.collection("reservas").document().id,
                lugarId = lugarId,
                usuarioId = usuarioId,
                fechaHora = fechaHora,
                cantidad = cantidad
            )
            firestore.collection("reservas").document(reserva.id).set(reserva)
                .addOnSuccessListener { onResult(true, null) }
                .addOnFailureListener { e -> onResult(false, e.message) }
        }
    }


    fun addReview(textReview: String, lugarId: String, onComplete: (success: Boolean) -> Unit) {
        val currentUser = auth.currentUser
        if (textReview.isBlank()) {
            onComplete(false)
            return
        }
        currentUser?.let { user ->
            _isSendingReview.value = true

            val userDocRef = firestore.collection("users").document(user.uid)

            userDocRef.get()
                .addOnSuccessListener { document ->
                    val userName = if (document.exists()) {
                        document.getString("name") ?: "Anónimo"
                    } else {
                        "Anónimo"
                    }

                    val documentRef = firestore.collection("reviews").document()

                    val review = Review(
                        id = documentRef.id,
                        lugarId = lugarId,
                        comentario = textReview,
                        usuarioId = user.uid,
                        usuarioNombre = userName,
                        fecha = System.currentTimeMillis()
                    )

                    documentRef.set(review)
                        .addOnSuccessListener {
                            Log.d("ReviewViewModel", "Reseña agregada con ID: ${documentRef.id}")
                            loadReviews(lugarId)
                            _isSendingReview.value = false
                            onComplete(true)
                        }
                        .addOnFailureListener { exception ->
                            Log.e("ReviewViewModel", "Error al agregar reseña: $exception")
                            _isSendingReview.value = false
                            onComplete(false)
                        }
                }
                .addOnFailureListener { exception ->
                    Log.e("ReviewViewModel", "Error al obtener nombre de usuario: $exception")
                    val documentRef = firestore.collection("reviews").document()

                    val review = Review(
                        id = documentRef.id,
                        lugarId = lugarId,
                        comentario = textReview,
                        usuarioId = user.uid,
                        usuarioNombre = "Anónimo",
                        fecha = System.currentTimeMillis()
                    )

                    documentRef.set(review)
                        .addOnSuccessListener {
                            Log.d("ReviewViewModel", "Reseña agregada con ID: ${documentRef.id}")
                            loadReviews(lugarId)
                            _isSendingReview.value = false
                            onComplete(true)
                        }
                        .addOnFailureListener { e ->
                            Log.e("ReviewViewModel", "Error al agregar reseña tras fallo en usuario: $e")
                            _isSendingReview.value = false
                            onComplete(false)
                        }
                }
        }
    }


    fun loadReviews(lugarId: String) {
        val currentUserId = auth.currentUser?.uid
        firestore.collection("reviews")
            .whereEqualTo("lugarId", lugarId)
            .orderBy("fecha", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    Log.e("LugarViewModel", "Error al escuchar reseñas: $exception")
                    _state.update { it.copy(errorMessage = "Error al cargar reseñas: ${exception.message}") }
                    return@addSnapshotListener
                }

                if (snapshot != null && !snapshot.isEmpty) {
                    val reviewList = snapshot.documents.map { document ->
                        val usuarioId = document.getString("usuarioId") ?: ""
                        Review(
                            id = document.id,
                            lugarId = document.getString("lugarId") ?: lugarId,
                            comentario = document.getString("comentario") ?: "",
                            usuarioId = usuarioId,
                            usuarioNombre = document.getString("usuarioNombre") ?: "",
                            fecha = document.getLong("fecha") ?: System.currentTimeMillis(),
                            canDelete = usuarioId == currentUserId
                        )
                    }
                    _state.update { it.copy(reviews = reviewList, errorMessage = null) }
                } else {
                    _state.update { it.copy(reviews = emptyList(), errorMessage = null) }
                }
            }
    }


    fun deleteReview(reviewId: String, lugarId: String, onResult: (success: Boolean, errorMsg: String?) -> Unit) {
        val currentUser = auth.currentUser
        currentUser?.let { user ->
            firestore.collection("reviews").document(reviewId)
                .get()
                .addOnSuccessListener { document ->
                    val usuarioId = document.getString("usuarioId")
                    if (usuarioId == user.uid) {
                        firestore.collection("reviews").document(reviewId)
                            .delete()
                            .addOnSuccessListener {
                                Log.d("LugarViewModel", "Reseña eliminada con éxito")
                                onResult(true, null)
                                loadReviews(lugarId) // Recarga la lista de reseñas del lugar
                            }
                            .addOnFailureListener { exception ->
                                Log.e("LugarViewModel", "Error al eliminar reseña: $exception")
                                onResult(false, exception.message)
                            }
                    } else {
                        Log.d("LugarViewModel", "No se puede eliminar esta reseña. No pertenece al usuario.")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("LugarViewModel", "Error al obtener la reseña para eliminar: $exception")
                }
        }
    }

}





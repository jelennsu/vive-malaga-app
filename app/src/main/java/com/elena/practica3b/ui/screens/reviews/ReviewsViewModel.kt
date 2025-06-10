package com.elena.practica3b.ui.screens.reviews

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

// ViewModel para manejar la lógica de las reseñas:
// Este ViewModel se encarga de agregar, cargar y eliminar reseñas desde Firebase Firestore.
// - La función `addReview` agrega una nueva reseña a la colección "reviews" en Firestore,
// asociándola al usuario logueado.
// - La función `loadReviews` obtiene todas las reseñas almacenadas en Firestore y las guarda
// en el estado `reviews` para ser mostradas en la UI.
// - La función `deleteReview` permite eliminar una reseña si el UID del usuario logueado
// coincide con el UID asociado a la reseña.

@HiltViewModel
class ReviewsViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private val _reviews = MutableStateFlow<List<ReviewState>>(emptyList())
    val reviews: StateFlow<List<ReviewState>> = _reviews


    fun addReview(review: ReviewState) {
        val currentUser = auth.currentUser
        currentUser?.let { user ->
            val reviewData = hashMapOf(
                "title" to review.title,
                "content" to review.content,
                "userName" to review.userName,
                "userUID" to user.uid
            )

            firestore.collection("reviews")
                .add(reviewData)
                .addOnSuccessListener { documentReference ->
                    Log.d("ReviewViewModel", "Reseña agregada con ID: ${documentReference.id}")
                    loadReviews()  // Recargar las reseñas
                }
                .addOnFailureListener { exception ->
                    Log.e("ReviewViewModel", "Error al agregar reseña: $exception")
                }
        }
    }

    fun loadReviews() {
        firestore.collection("reviews")
            .get()
            .addOnSuccessListener { result ->
                val reviewList = result.map { document ->
                    val title = document.getString("title") ?: ""
                    val content = document.getString("content") ?: ""
                    val userName = document.getString("userName") ?: ""
                    val userUID = document.getString("userUID") ?: ""
                    ReviewState(
                        id = document.id,
                        title = title,
                        content = content,
                        userName = userName,
                        userUID = userUID
                    )
                }
                _reviews.value = reviewList
            }
            .addOnFailureListener { exception ->
                Log.e("ReviewViewModel", "Error al cargar reseñas: $exception")
            }
    }


    // Función para eliminar una reseña
    fun deleteReview(reviewId: String) {
        val currentUser = auth.currentUser
        currentUser?.let { user ->
            // Verificamos que el UID de la reseña coincida con el UID del usuario logueado
            firestore.collection("reviews").document(reviewId)
                .get()
                .addOnSuccessListener { document ->
                    val userUID = document.getString("userUID")
                    if (userUID == user.uid) {
                        // Si los UID coinciden, eliminamos la reseña
                        firestore.collection("reviews").document(reviewId)
                            .delete()
                            .addOnSuccessListener {
                                Log.d("ReviewViewModel", "Reseña eliminada")
                                loadReviews()  // Recargar las reseñas
                            }
                            .addOnFailureListener { exception ->
                                Log.e("ReviewViewModel", "Error al eliminar reseña: $exception")
                            }
                    } else {
                        Log.d("ReviewViewModel", "No se puede eliminar esta reseña. No pertenece al usuario.")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("ReviewViewModel", "Error al obtener la reseña para eliminar: $exception")
                }
        }
    }
}

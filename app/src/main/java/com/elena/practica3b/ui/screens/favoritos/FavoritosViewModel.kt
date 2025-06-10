package com.elena.practica3b.ui.screens.favoritos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class FavoritosViewModel @Inject constructor(
    firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val favoritosCollection = firestore.collection("favoritos")

    private val currentUserId: String?
        get() = auth.currentUser?.uid

    val favoritos: StateFlow<List<Favorito>> = if (currentUserId == null) {
        // Usuario no autenticado, flujo vacío
        MutableStateFlow(emptyList())
    } else {
        callbackFlow {
            val listener = favoritosCollection
                .whereEqualTo("usuarioId", currentUserId)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        close(error)
                        return@addSnapshotListener
                    }
                    val favoritos = snapshot?.documents?.mapNotNull {
                        it.toObject(Favorito::class.java)
                    } ?: emptyList()
                    trySend(favoritos).isSuccess
                }
            awaitClose { listener.remove() }
        }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    }

    suspend fun addFavorito(favorito: Favorito) {
        favoritosCollection
            .add(favorito)
            .await()
    }

    suspend fun removeFavorito(favorito: Favorito) {
        val currentUser = currentUserId ?: return
        val querySnapshot = favoritosCollection
            .whereEqualTo("usuarioId", currentUser)
            .whereEqualTo("lugarId", favorito.lugarId)
            .get()
            .await()

        for (doc in querySnapshot.documents) {
            favoritosCollection.document(doc.id).delete().await()
        }
    }

    suspend fun isFavorito(lugarId: String): Boolean {
        val currentUser = currentUserId ?: return false
        val snapshot = favoritosCollection
            .whereEqualTo("usuarioId", currentUser)
            .whereEqualTo("lugarId", lugarId)
            .get()
            .await()
        return !snapshot.isEmpty
    }
}

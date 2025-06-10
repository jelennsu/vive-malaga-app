package com.elena.practica3b.ui.screens.drawer

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elena.practica3b.R
import com.elena.practica3b.data.repository.HistorialRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class DrawerViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage,
    private val historialRepository: HistorialRepository // Inyecta el repo aquí
) : ViewModel() {

    private val _state = mutableStateOf(DrawerState())
    val state: State<DrawerState> get() = _state

    private var userListener: ListenerRegistration? = null

    private val authListener = FirebaseAuth.AuthStateListener {
        observeCurrentUser()
    }

    init {
        auth.addAuthStateListener(authListener)
        observeCurrentUser()
    }

    private fun observeCurrentUser() {
        userListener?.remove()
        val currentUser = auth.currentUser
        if (currentUser == null) {
            _state.value = DrawerState(userName = "")
            return
        }
        userListener = firestore.collection("users")
            .document(currentUser.uid)
            .addSnapshotListener { snapshot, error ->
                if (error != null) return@addSnapshotListener
                if (snapshot != null && snapshot.exists()) {
                    val userName = snapshot.getString("name") ?: ""
                    val profileImageUrl = snapshot.getString("profileImageUrl")
                    _state.value = DrawerState(userName = userName, profileImageUrl = profileImageUrl)
                } else {
                    _state.value = DrawerState(userName = "")
                }
            }
    }

    fun logout() {
        auth.signOut()
        _state.value = DrawerState(userName = "")
    }

    fun deleteAccount(onResult: (Boolean, Int) -> Unit) {
        val user = auth.currentUser
        if (user == null) {
            onResult(false, R.string.user_not_logged_in)
            return
        }

        viewModelScope.launch {
            try {
                // 1. Borrar favoritos en Firestore (batch)
                val batch = firestore.batch()
                val favoritosSnapshot = firestore.collection("favoritos")
                    .whereEqualTo("usuarioId", user.uid)
                    .get()
                    .await()
                for (doc in favoritosSnapshot.documents) {
                    batch.delete(doc.reference)
                }
                batch.commit().await()

                // 2. Borrar foto si no es la predeterminada
                val profileImageUrl = state.value.profileImageUrl
                val defaultUrlRef = storage.reference.child("profile_images/userdefault.jpg")
                val defaultUrl = defaultUrlRef.downloadUrl.await().toString()
                if (!profileImageUrl.isNullOrEmpty() && profileImageUrl != defaultUrl) {
                    val photoRef = storage.getReferenceFromUrl(profileImageUrl)
                    photoRef.delete().await()
                }

                // 3. Borrar historial local con historialRepository
                historialRepository.borrarTodo(user.uid)

                // 4. Borrar documento usuario Firestore
                firestore.collection("users").document(user.uid).delete().await()

                // 5. Borrar cuenta Firebase Auth
                user.delete().await()

                onResult(true, R.string.account_deleted_success)
            } catch (e: Exception) {
                onResult(false, R.string.account_delete_firestore_failed)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        userListener?.remove()
        auth.removeAuthStateListener(authListener)
    }
}



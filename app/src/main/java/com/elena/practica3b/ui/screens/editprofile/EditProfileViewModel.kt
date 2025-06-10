package com.elena.practica3b.ui.screens.editprofile

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val storage: FirebaseStorage
) : ViewModel() {

    private val _eventChannel = Channel<EditProfileUiEvent>()
    val eventFlow = _eventChannel.receiveAsFlow()


    var state by mutableStateOf(EditProfileState())
        private set

    init {
        loadUserData()
    }

    private fun loadUserData() {
        val uid = auth.currentUser?.uid ?: return
        firestore.collection("users").document(uid).get()
            .addOnSuccessListener { doc ->
                state = state.copy(
                    name = doc.getString("name") ?: "",
                    phone = doc.getString("phoneNumber") ?: "",
                    imageUrl = doc.getString("profileImageUrl") ?: "",
                    isLoading = false
                )
            }
            .addOnFailureListener {
                Timber.e(it, "Error al cargar los datos del usuario")
                state = state.copy(isLoading = false)
            }
    }

    fun onNameChanged(newName: String) {
        state = state.copy(name = newName)
    }

    fun onPhoneChanged(newPhone: String) {
        state = state.copy(phone = newPhone)
    }

    fun updateUserData(newName: String, newPhone: String, newImageUri: Uri?) {
        val uid = auth.currentUser?.uid ?: return
        state = state.copy(isLoading = true)

        viewModelScope.launch {
            try {
                var newImageUrl = state.imageUrl

                if (newImageUri != null) {
                    val imageRef = storage.reference.child("profile_images/$uid.jpg")
                    imageRef.putFile(newImageUri).await()
                    newImageUrl = imageRef.downloadUrl.await().toString()
                }

                val updates = mapOf(
                    "name" to newName,
                    "phoneNumber" to newPhone,
                    "profileImageUrl" to newImageUrl
                )

                firestore.collection("users").document(uid).update(updates).await()

                state = state.copy(
                    name = newName,
                    phone = newPhone,
                    imageUrl = newImageUrl,
                    isLoading = false
                )
                _eventChannel.send(EditProfileUiEvent.ProfileUpdated)


            } catch (e: Exception) {
                Timber.e(e, "Error al actualizar el perfil")
                state = state.copy(isLoading = false)
                _eventChannel.send(EditProfileUiEvent.ShowError("Error al actualizar el perfil"))

            }
        }
    }

    fun deleteProfileImage() {
        val uid = auth.currentUser?.uid ?: return
        viewModelScope.launch {
            try {
                if (!state.imageUrl.contains("userdefault.jpg")) {
                    val imageRef = storage.reference.child("profile_images/$uid.jpg")
                    try {
                        imageRef.delete().await()
                    } catch (e: StorageException) {
                        if (e.errorCode == StorageException.ERROR_OBJECT_NOT_FOUND) {
                            Timber.w("La imagen personalizada no existe en Storage.")
                        } else {
                            throw e
                        }
                    }
                }

                // Obtener URL descargable de la imagen predeterminada
                val defaultUrlRef = storage.reference.child("profile_images/userdefault.jpg")
                val defaultUrl = defaultUrlRef.downloadUrl.await().toString()

                firestore.collection("users").document(uid)
                    .update("profileImageUrl", defaultUrl).await()

                state = state.copy(imageUrl = defaultUrl)
                _eventChannel.send(EditProfileUiEvent.ProfileUpdated)

            } catch (e: Exception) {
                Timber.e(e, "Error al eliminar la imagen")
                _eventChannel.send(EditProfileUiEvent.ShowError("Error al eliminar la imagen"))
            }
        }
    }


}


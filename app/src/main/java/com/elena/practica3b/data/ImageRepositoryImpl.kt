package com.elena.practica3b.data

import android.net.Uri
import com.elena.practica3b.model.Response
import com.elena.practica3b.repository.AddImageToStorageResponse
import com.elena.practica3b.repository.AddImageUrlToFirestoreResponse
import com.elena.practica3b.repository.GetImageFromFirestoreResponse
import com.elena.practica3b.repository.ImageRepository
import com.elena.practica3b.utils.Constants.IMAGES
import com.elena.practica3b.utils.Constants.URL
import com.elena.practica3b.utils.Constants.USER_ID
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/**
 * Implementación del repositorio de imágenes que maneja la carga, almacenamiento y recuperación
 * de imágenes en Firebase Storage y Firestore.
 *
 * Funcionalidades principales:
 * - Subir imágenes a Firebase Storage y obtener la URL de descarga.
 * - Guardar la URL de la imagen en Firestore junto con el ID del usuario.
 * - Recuperar todas las imágenes almacenadas en Firestore con sus respectivos IDs de usuario.
 *
 * Métodos:
 * - addImageToFirebaseStorage(imageUri: Uri): Sube la imagen a Firebase Storage y devuelve su URL.
 * - addImageUrlToFirestore(download: Uri): Guarda la URL de la imagen en Firestore con el ID del usuario.
 * - getImageUrlFromFirestore(): Obtiene la lista de imágenes almacenadas en Firestore.
 *
 * Se utiliza Firebase Authentication para asociar las imágenes con el usuario autenticado.
 * Se manejan excepciones para garantizar respuestas adecuadas en caso de error.
 */

class ImageRepositoryImpl(
    private val auth: FirebaseAuth,
    private val storage: FirebaseStorage,
    private val db: FirebaseFirestore
) : ImageRepository {

    override suspend fun addImageToFirebaseStorage(imageUri: Uri): AddImageToStorageResponse {
        return try {
            val userId = auth.currentUser?.uid ?: throw Exception("Usuario no autenticado")
            val ref = storage.reference.child("profile_images/$userId.jpg")
            ref.putFile(imageUri).await() // requiere coroutines-play-services
            val downloadUrl = ref.downloadUrl.await()
            Response.Success(downloadUrl)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override suspend fun addImageUrlToFirestore(imageUri: Uri): AddImageUrlToFirestoreResponse {
        return try {
            val userId = auth.currentUser?.uid ?: throw Exception("Usuario no autenticado")
            val imageUrl = imageUri.toString()
            db.collection("users").document(userId)
                .update("profileImageUrl", imageUrl)
                .await()
            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override suspend fun getImageUrlFromFirestore(): GetImageFromFirestoreResponse {
        return try {
            val userId = auth.currentUser?.uid ?: throw Exception("Usuario no autenticado")
            val snapshot = db.collection("users").document(userId).get().await()
            val url = snapshot.getString("profileImageUrl") ?: ""
            Response.Success(listOf(userId to url))
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }
}


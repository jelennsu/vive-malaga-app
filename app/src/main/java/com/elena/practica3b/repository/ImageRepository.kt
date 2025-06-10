package com.elena.practica3b.repository

import android.net.Uri
import com.elena.practica3b.model.Response

/**
 * Alias de tipos para representar las respuestas de las operaciones de almacenamiento de imágenes.
 */
typealias AddImageToStorageResponse = Response<Uri> /**< Respuesta al agregar una imagen al almacenamiento de Firebase. */
typealias AddImageUrlToFirestoreResponse = Response<Boolean> /**< Respuesta al agregar la URL de una imagen a Firestore. */
typealias GetImageFromFirestoreResponse = Response<List<Pair<String, String>>> /**< Respuesta al obtener imágenes desde Firestore. */

/**
 * Interfaz que define las operaciones del repositorio de imágenes.
 */
interface ImageRepository {

    /**
     * Sube una imagen a Firebase Storage.
     *
     * @param imageUri URI de la imagen a subir.
     * @return [AddImageToStorageResponse] con la URI de la imagen almacenada o un error.
     */
    suspend fun addImageToFirebaseStorage(imageUri: Uri): AddImageToStorageResponse

    /**
     * Guarda la URL de una imagen en Firestore.
     *
     * @param imageUri URI de la imagen cuya URL se almacenará.
     * @return [AddImageUrlToFirestoreResponse] indicando éxito o error.
     */
    suspend fun addImageUrlToFirestore(imageUri: Uri): AddImageUrlToFirestoreResponse

    /**
     * Obtiene las URLs de las imágenes almacenadas en Firestore.
     *
     * @return [GetImageFromFirestoreResponse] con una lista de pares (ID, URL) o un error.
     */
    suspend fun getImageUrlFromFirestore(): GetImageFromFirestoreResponse

}


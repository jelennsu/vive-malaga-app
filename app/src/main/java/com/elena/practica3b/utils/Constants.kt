package com.elena.practica3b.utils

/**
 * Objeto que contiene las constantes utilizadas en la aplicación para la gestión
 * de imágenes y datos de usuario en Firebase.
 */
object Constants {
    /** Nombre de la colección o nodo donde se almacenan las imágenes. */
    const val IMAGES = "images"

    /** Clave utilizada para almacenar y recuperar la URL de una imagen en Firestore. */
    const val URL = "url"

    /** Clave utilizada para identificar al usuario en la base de datos. */
    const val USER_ID = "userId"
}

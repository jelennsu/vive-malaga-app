package com.elena.practica3b.ui.screens.reviews

/**
 * Representa una reseña escrita por un usuario sobre un lugar.
 */
data class Review(
    val id: String = "",
    val lugarId: String = "",
    val usuarioId: String = "",
    val usuarioNombre: String = "",
    val comentario: String = "",
    val fecha: Long = System.currentTimeMillis(),
    val canDelete: Boolean = false
)


package com.elena.practica3b.ui.screens.lugar.data


data class Review(
    val id: String = "",
    val lugarId: String = "",
    val usuarioId: String = "",
    val usuarioNombre: String = "",
    val comentario: String = "",
    val fecha: Long = System.currentTimeMillis(),
    val canDelete: Boolean = false
)


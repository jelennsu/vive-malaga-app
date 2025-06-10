package com.elena.practica3b.ui.screens.reviews


// Representa el estado de una reseña con sus datos: id, título, contenido, nombre de usuario
// y UID del usuario.

data class ReviewState(
    val id: String = "",
    val title: String = "",
    val content: String = "",
    val userName: String = "",
    val userUID: String = ""
)

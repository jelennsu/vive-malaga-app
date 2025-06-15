package com.elena.practica3b.ui.screens.favoritos

/*
 * Data class que representa un lugar favorito de un usuario,
 * con información básica necesaria para mostrarlo en la UI.
 */

data class Favorito(
    val usuarioId: String = "",
    val lugarId: String = "",
    val nombre: String = "",
    val imagenUrl: String = ""
)

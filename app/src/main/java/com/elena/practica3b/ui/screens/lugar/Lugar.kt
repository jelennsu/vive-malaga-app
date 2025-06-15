package com.elena.practica3b.ui.screens.lugar

/**
 * Representa un lugar de interés con sus datos básicos para mostrar en la app.
 */
data class Lugar(
    val id: String = "",
    val nombre: String = "",
    val categoria: String = "",
    val descripcion: String = "",
    val localidad: String = "",
    val direccion: String = "",
    val imagenUrl: String = ""
)


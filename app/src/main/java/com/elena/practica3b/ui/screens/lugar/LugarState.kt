package com.elena.practica3b.ui.screens.lugar

import com.elena.practica3b.ui.screens.reviews.Review

/**
 * Estado de la pantalla Lugar, que incluye la carga, el lugar, errores y las reseñas asociadas.
 */
data class LugarState(
    val isLoading: Boolean = true,
    val lugar: Lugar? = null,
    val errorMessage: String? = null,
    val reviews: List<Review> = emptyList()
)

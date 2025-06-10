package com.elena.practica3b.ui.screens.lugar

import com.elena.practica3b.ui.screens.lugar.data.Lugar
import com.elena.practica3b.ui.screens.lugar.data.Review

data class LugarState(
    val isLoading: Boolean = true,
    val lugar: Lugar? = null,
    val errorMessage: String? = null,
    val reviews: List<Review> = emptyList()
)

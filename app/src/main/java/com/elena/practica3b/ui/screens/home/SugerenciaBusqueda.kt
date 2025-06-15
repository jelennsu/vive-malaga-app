package com.elena.practica3b.ui.screens.home

import com.elena.practica3b.ui.screens.lugar.Lugar

/*
 * Modelo sellado que representa los distintos tipos de sugerencias
 * que pueden aparecer en la búsqueda: lugar, categoría o localidad.
 */
sealed class SugerenciaBusqueda {
    data class PorLugar(val lugar: Lugar) : SugerenciaBusqueda()
    data class PorCategoria(val categoria: String) : SugerenciaBusqueda()
    data class PorLocalidad(val localidad: String) : SugerenciaBusqueda()
}

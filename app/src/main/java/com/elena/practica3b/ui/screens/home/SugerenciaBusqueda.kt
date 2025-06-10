package com.elena.practica3b.ui.screens.home

import com.elena.practica3b.ui.screens.lugar.data.Lugar

sealed class SugerenciaBusqueda {
    data class PorLugar(val lugar: Lugar) : SugerenciaBusqueda()
    data class PorCategoria(val categoria: String) : SugerenciaBusqueda()
    data class PorLocalidad(val localidad: String) : SugerenciaBusqueda()
}

package com.elena.practica3b.ui.screens.lugar.data

import com.google.firebase.firestore.Exclude

data class Reserva(
    val id: String = "",
    val lugarId: String = "",
    val usuarioId: String = "",
    val fechaHora: Long = 0L,
    val cantidad: Int = 0,

    @get:Exclude
    var nombreLugar: String = ""
)

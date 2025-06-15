package com.elena.practica3b.ui.screens.reservas

import com.google.firebase.firestore.Exclude

/**
 * Representa una reserva de un usuario para un lugar en una fecha y hora específicas.
 * Contiene datos básicos para mostrar y gestionar la reserva.
 */
data class Reserva(
    val id: String = "",
    val lugarId: String = "",
    val usuarioId: String = "",
    val fechaHora: Long = 0L,
    val cantidad: Int = 0,

    @get:Exclude
    var nombreLugar: String = ""
)

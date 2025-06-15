package com.elena.practica3b.ui.screens.editprofile

/*
 * Modelo de estado para la pantalla de edición de perfil,
 * contiene datos del usuario: nombre, teléfono, URL de la imagen de perfil
 * y flag para indicar si hay una operación en curso (carga/guardado).
 */
data class EditProfileState(
    val name: String = "",
    val phone: String = "",
    val imageUrl: String = "",
    val isLoading: Boolean = false
)

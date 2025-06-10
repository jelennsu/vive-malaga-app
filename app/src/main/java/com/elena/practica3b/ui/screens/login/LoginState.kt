package com.elena.practica3b.ui.screens.login

import androidx.annotation.StringRes

/**
 * Representa el estado del inicio de sesión en la aplicación.
 *
 * @property email Dirección de correo electrónico ingresada por el usuario.
 * @property password Contraseña ingresada por el usuario.
 * @property successLogin Indica si el inicio de sesión fue exitoso.
 * @property displayProgressBar Indica si se debe mostrar la barra de progreso durante el inicio de sesión.
 * @property errorMessage Mensaje de error en caso de fallo en el inicio de sesión, referenciado como un recurso de cadena.
 */
data class LoginState(
    val email: String = "",
    val password: String = "",
    val successLogin: Boolean = false,
    val displayProgressBar: Boolean = false,
    @StringRes val errorMessage: Int? = null
)

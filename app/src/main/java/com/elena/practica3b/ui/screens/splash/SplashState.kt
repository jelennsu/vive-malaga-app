package com.elena.practica3b.ui.screens.splash

/**
 * Estado de la pantalla Splash que refleja el estado de autenticación del usuario.
 *
 * @property isUserLoggedIn Indica si el usuario está autenticado.
 *   - `true` si el usuario ha iniciado sesión.
 *   - `false` si el usuario no está autenticado.
 *   - `null` si el estado aún no ha sido determinado.
 */

data class SplashState(
    val isUserLoggedIn: Boolean? = null
)

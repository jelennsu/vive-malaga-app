package com.elena.practica3b.ui.screens.splash

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.compose.runtime.State

/**
 * ViewModel para la pantalla Splash que gestiona el estado de autenticación del usuario.
 *
 * Utiliza FirebaseAuth para comprobar si hay un usuario actualmente autenticado.
 * Expone un estado observable (`SplashState`) que indica si el usuario ha iniciado sesión.
 *
 * Funcionalidad principal:
 * - `checkUserSession()`: verifica la sesión actual y actualiza el estado en consecuencia.
 *
 * La pantalla Splash puede observar este estado para decidir la navegación inicial.
 */
@HiltViewModel
class SplashViewModel @Inject constructor(
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _state = mutableStateOf(SplashState())
    val state: State<SplashState> get() = _state

    fun checkUserSession() {
        val isLogged = auth.currentUser != null
        _state.value = _state.value.copy(isUserLoggedIn = isLogged)
    }
}


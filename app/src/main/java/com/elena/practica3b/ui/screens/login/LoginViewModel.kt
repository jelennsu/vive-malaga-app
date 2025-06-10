package com.elena.practica3b.ui.screens.login

import android.util.Patterns
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.elena.practica3b.R
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * ViewModel que maneja la lógica del inicio de sesión.
 *
 * Se encarga de validar los campos de entrada, autenticar al usuario mediante Firebase
 * y actualizar el estado de la interfaz de usuario.
 *
 * @property firebaseAuth Instancia de FirebaseAuth utilizada para la autenticación del usuario.
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    /** Estado actual de la pantalla de inicio de sesión. */
    private val _state: MutableState<LoginState> = mutableStateOf(LoginState())

    /** Estado expuesto de solo lectura para la UI. */
    val state: State<LoginState> get() = _state

    /**
     * Inicia sesión con el correo y contraseña proporcionados.
     *
     * Valida los campos antes de intentar autenticarse con Firebase.
     * Si los datos son incorrectos o hay un error en la autenticación, actualiza el estado con un mensaje de error.
     *
     * @param email Correo electrónico del usuario.
     * @param password Contraseña del usuario.
     */
    fun login(email: String, password: String) {
        val errorMessage = when {
            email.isBlank() || password.isBlank() -> R.string.error_input_empty
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> R.string.error_not_a_valid_email
            else -> null
        }

        errorMessage?.let {
            _state.value = state.value.copy(errorMessage = it)
            return
        }

        _state.value = state.value.copy(displayProgressBar = true)

        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                _state.value = state.value.copy(displayProgressBar = false)
                if (task.isSuccessful) {
                    _state.value = state.value.copy(successLogin = true)
                } else {
                    val exception = task.exception
                    val errorMsg = when (exception) {
                        is FirebaseAuthInvalidUserException -> R.string.error_user_not_found
                        is FirebaseAuthInvalidCredentialsException -> R.string.error_invalid_credentials
                        is FirebaseNetworkException -> R.string.error_network
                        else -> R.string.error_invalid_credentials
                    }
                    _state.value = state.value.copy(errorMessage = errorMsg)
                }
            }
    }

    /**
     * Oculta el cuadro de diálogo de error restableciendo el mensaje de error.
     */
    fun hideDialog() {
        _state.value = state.value.copy(errorMessage = null)
    }

}


package com.elena.practica3b.ui.screens.registration

import android.util.Patterns
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elena.practica3b.R
import com.elena.practica3b.repository.ImageRepository
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

// ViewModel encargado de manejar la lógica del registro de usuario.
// Realiza la validación de los campos ingresados, como nombre, correo electrónico, número de
// teléfono, contraseña y confirmación de contraseña. Si los datos son válidos, intenta registrar
// al usuario en Firebase y guardar su información adicional en Firestore. Si ocurre un error,
// actualiza el estado con el mensaje de error correspondiente. También maneja el caso en que el
// correo electrónico ya esté registrado.


@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage,
) : ViewModel() {


    private val _state: MutableState<RegisterState> = mutableStateOf(RegisterState())
    val state: State<RegisterState> get() = _state

    fun register(
        name: String,
        email: String,
        phoneNumber: String,
        password: String,
        confirmPassword: String
    ) {
        val passwordPattern = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^a-zA-Z0-9]).{6,}$")

        val errorMessage = when {
            name.isBlank() || email.isBlank() || phoneNumber.isBlank() || password.isBlank() || confirmPassword.isBlank() -> {
                R.string.error_input_empty
            }

            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                R.string.error_not_a_valid_email
            }

            !Patterns.PHONE.matcher(phoneNumber).matches() -> {
                R.string.error_not_a_valid_phone_number
            }

            password != confirmPassword -> {
                R.string.error_incorrectly_repeated_password
            }

            !password.matches(passwordPattern) -> {
                R.string.error_password_format_invalid
            }

            else -> null
        }

        errorMessage?.let {
            _state.value = _state.value.copy(errorMessage = it)
            return
        }

        viewModelScope.launch {
            _state.value = _state.value.copy(displayProgressBar = true)

            try {
                val authResult = auth.createUserWithEmailAndPassword(email, password).await()
                val firebaseUser = authResult.user

                if (firebaseUser != null) {

                    val defaultRef = storage
                        .reference
                        .child("profile_images/userdefault.jpg")

                    val defaultProfileImageUrl = defaultRef.downloadUrl.await().toString()

                    val userData = hashMapOf(
                        "uid" to firebaseUser.uid,
                        "name" to name,
                        "email" to email,
                        "phoneNumber" to phoneNumber,
                        "profileImageUrl" to defaultProfileImageUrl
                    )
                    try {
                        firestore.collection("users").document(firebaseUser.uid)
                            .set(userData).await()

                        auth.signOut()

                        _state.value = _state.value.copy(
                            successRegister = true,
                            displayProgressBar = false,
                            errorMessage = null
                        )
                    } catch (e: Exception) {
                        Timber.tag("RegisterViewModel").e(e, "Error guardando datos en Firestore")
                        _state.value = _state.value.copy(
                            errorMessage = R.string.error_saving_user,
                            displayProgressBar = false
                        )
                    }
                } else {
                    _state.value = _state.value.copy(
                        errorMessage = R.string.error_registration_failed,
                        displayProgressBar = false
                    )
                }
            } catch (e: FirebaseAuthUserCollisionException) {
                Timber.tag("RegisterViewModel").w(e, "Correo ya registrado: $email")
                _state.value = _state.value.copy(
                    errorMessage = R.string.error_email_already_registered,
                    displayProgressBar = false
                )
            } catch (e: FirebaseNetworkException) {
                Timber.tag("RegisterViewModel").w(e, "Sin conexión a Internet")
                _state.value = _state.value.copy(
                    errorMessage = R.string.error_network,
                    displayProgressBar = false
                )
            }
            catch (e: Exception) {
                Timber.tag("RegisterViewModel").e(e, "Error inesperado durante el registro")
                _state.value = _state.value.copy(
                    errorMessage = R.string.error_unknown,
                    displayProgressBar = false
                )
            }
        }
    }

    fun hideDialog() {
        _state.value = _state.value.copy(
            errorMessage = null,
            successRegister = false
        )
    }
}


package com.elena.practica3b.ui.screens.registration

import androidx.annotation.StringRes

// Estado del registro: éxito, barra de progreso y mensaje de error.

data class RegisterState(
    val successRegister: Boolean = false,
    val displayProgressBar: Boolean = false,
    @StringRes val errorMessage: Int? = null
)
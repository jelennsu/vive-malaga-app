package com.elena.practica3b.ui.screens.drawer

// Estado del Drawer que almacena datos de usuario, carga y errores para gestionar la UI del menú lateral.
data class DrawerState(
    val userName: String = "Invitado",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val profileImageUrl: String? = null
)

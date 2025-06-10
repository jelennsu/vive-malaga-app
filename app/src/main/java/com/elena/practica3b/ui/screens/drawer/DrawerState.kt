package com.elena.practica3b.ui.screens.drawer

data class DrawerState(
    val userName: String = "Invitado",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val profileImageUrl: String? = null
)

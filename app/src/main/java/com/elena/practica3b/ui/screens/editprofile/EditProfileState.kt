package com.elena.practica3b.ui.screens.editprofile

data class EditProfileState(
    val name: String = "",
    val phone: String = "",
    val imageUrl: String = "",
    val isLoading: Boolean = false
)

package com.elena.practica3b.ui.screens.editprofile

sealed class EditProfileUiEvent {
    object ProfileUpdated : EditProfileUiEvent()
    data class ShowError(val message: String) : EditProfileUiEvent()
}

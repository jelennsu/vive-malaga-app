package com.elena.practica3b.ui.screens.editprofile

/*
 * Eventos de UI para la pantalla de edición de perfil,
 * que notifican éxito de actualización o muestran errores con mensaje.
 */

sealed class EditProfileUiEvent {
    object ProfileUpdated : EditProfileUiEvent()
    data class ShowError(val message: String) : EditProfileUiEvent()
}

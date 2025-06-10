package com.elena.practica3b.ui.screens.splash

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.compose.runtime.State

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


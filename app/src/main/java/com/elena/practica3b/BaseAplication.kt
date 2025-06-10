package com.elena.practica3b

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

// Clase base de la aplicación que se utiliza para la inyección de dependencias con Hilt.

@HiltAndroidApp
class BaseApplication : Application() {
}
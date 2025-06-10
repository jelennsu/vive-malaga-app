package com.elena.practica3b.navigation

// Definición de las pantallas de la app con sus rutas de navegación.

sealed class AppScreens(val route: String) {
    data object SplashScreen : AppScreens("splash_screen")
    data object LoginScreen : AppScreens("login_screen")
    data object RegistrationScreen : AppScreens("registration_screen")
    data object HomeScreen : AppScreens("home_screen")
    data object EditProfileScreen : AppScreens("edit_profile_screen")
    data object ReservasScreen : AppScreens("reservas_screen")
    data object FavoritosScreen : AppScreens("favoritos_screen")
    data object LugarScreen : AppScreens("lugar_screen/{id}") {
        val baseRoute = "lugar_screen"
        fun createRoute(id: String) = "$baseRoute/$id"
    }
    data object CategoriaScreen: AppScreens("categoria_screen/{categoria_nombre}") {
        fun createRoute(categoriaNombre: String) = "categoria_screen/$categoriaNombre"
    }
    data object LocalidadScreen {
        fun createRoute(localidad: String) = "localidad_screen/${localidad}"
        const val route = "localidad_screen/{localidad}"
    }
    data object HistorialScreen : AppScreens("historial_screen")
}

fun getBaseRoute(route: String?): String {
    if (route == null) return ""
    val index = route.indexOf("/")
    return if (index == -1) route else route.substring(0, index)
}


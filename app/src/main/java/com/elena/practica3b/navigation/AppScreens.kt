package com.elena.practica3b.navigation

/**
 * Definición tipada y segura de las rutas de navegación de la app mediante un sealed class.
 * Cada pantalla representa una ruta única, con soporte para rutas parametrizadas y métodos
 * auxiliares para construir rutas dinámicas, mejorando la mantenibilidad y seguridad de la navegación.
 *
 * La función `getBaseRoute` extrae la ruta base ignorando parámetros para facilitar la gestión
 * de visibilidad y lógica de navegación según la pantalla activa.
 */
sealed class AppScreens(val route: String) {
    data object SplashScreen : AppScreens("splash_screen")
    data object LoginScreen : AppScreens("login_screen")
    data object RegistrationScreen : AppScreens("registration_screen")
    data object HomeScreen : AppScreens("home_screen")
    data object EditProfileScreen : AppScreens("edit_profile_screen")
    data object ReservasScreen : AppScreens("reservas_screen")
    data object FavoritosScreen : AppScreens("favoritos_screen")
    data object LugarScreen : AppScreens("lugar_screen/{id}") {
        const val baseRoute = "lugar_screen"
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


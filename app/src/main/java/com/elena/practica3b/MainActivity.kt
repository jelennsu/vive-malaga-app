package com.elena.practica3b

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.elena.practica3b.navigation.AppNavigationLandscape
import com.elena.practica3b.navigation.AppNavigationPortrait
import com.elena.practica3b.ui.theme.Practica3BTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Actividad principal de la aplicación que inicializa la inyección de dependencias con Hilt
 * y configura la interfaz de usuario según el tamaño de la ventana del dispositivo.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Practica3BTheme {
                val windowSizeClass = calculateWindowSizeClass(this)
                App(windowSizeClass)
            }
        }
    }
}

/**
 * Función composable que selecciona la navegación adecuada según el ancho de la pantalla.
 *
 * @param windowSize Clase que representa el tamaño de la ventana actual del dispositivo.
 */
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun App(windowSize: WindowSizeClass) {
    val navController = rememberNavController()
    when (windowSize.widthSizeClass) {
        WindowWidthSizeClass.Compact -> AppNavigationPortrait(navController)
        WindowWidthSizeClass.Expanded -> AppNavigationLandscape(navController)
        else -> AppNavigationPortrait(navController)
    }
}


/**
 * Vista previa de la navegación en orientación vertical (modo retrato).
 */
@OptIn(ExperimentalAnimationApi::class)
@Preview(widthDp = 360, heightDp = 640)
@Composable
fun PortraitPreview() {
    AppNavigationPortrait(rememberNavController())
}

/**
 * Vista previa de la navegación en orientación horizontal (modo paisaje).
 */
@Preview(widthDp = 640, heightDp = 360)
@Composable
fun LandscapePreview() {
    AppNavigationLandscape(rememberNavController())
}

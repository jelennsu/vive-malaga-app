package com.elena.practica3b.ui.screens.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.elena.practica3b.R
import com.elena.practica3b.navigation.AppScreens
import com.elena.practica3b.ui.theme.Practica3BTheme
import kotlinx.coroutines.delay

/**
 * Pantalla de bienvenida (Splash Screen) que muestra el logo y el texto "Vive Málaga".
 *
 * Muestra la imagen y texto durante 3 segundos antes de verificar la sesión del usuario
 * mediante el ViewModel. Según el estado de autenticación, navega a la pantalla Home o Login.
 *
 * Uso:
 * - `LaunchedEffect` para el delay inicial.
 * - Observa el estado de sesión para realizar la navegación.
 *
 * @param navController Controlador de navegación para gestionar las rutas.
 */

@Composable
fun SplashScreen(navController: NavHostController) {
    val viewModel: SplashViewModel = hiltViewModel()
    val state = viewModel.state.value

    LaunchedEffect(Unit) {
        delay(3000)
        viewModel.checkUserSession()
    }

    LaunchedEffect(state.isUserLoggedIn) {
        state.isUserLoggedIn?.let { isLoggedIn ->
            navController.popBackStack()
            val destination = if (isLoggedIn) {
                AppScreens.HomeScreen.route
            } else {
                AppScreens.LoginScreen.route
            }
            navController.navigate(destination)
        }
    }

    Splash()
}

@Composable
fun Splash() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(painter = painterResource(id = R.drawable.biznaga),
            contentDescription = "Splash Image",
            Modifier.size(200.dp, 200.dp),
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground))
        Text("Vive Málaga",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold)
    }

}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    Practica3BTheme {
        Splash()
    }
}
@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SplashScreenDarkPreview() {
    Practica3BTheme {
        Splash()
    }
}

package com.elena.practica3b.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.elena.practica3b.R
import com.elena.practica3b.navigation.AppScreens
import com.elena.practica3b.ui.theme.Practica3BTheme

// Componente BottomNavigation:
// Este componente crea una barra de navegación en la parte inferior de la pantalla.
// Utiliza `NavigationBar` de Material3 para proporcionar una barra de navegación con tres elementos:
// 1. Un ícono de "Inicio" que navega a la pantalla de inicio (HomeScreen).
// 2. Un ícono de "Reseñas" que navega a la pantalla de reseñas (ReviewsScreen).
// 3. Un ícono de "Galería" que navega a la pantalla de galería (GaleriaScreen).
// Cada ícono está acompañado de un texto que indica el nombre de la sección correspondiente.
// La barra de navegación está diseñada para resaltar el ítem seleccionado, con la opción de cambiar
// de pantalla al hacer clic en un ícono.

@Composable
fun BottomNavigation(navController: NavController, modifier: Modifier = Modifier) {
    val currentRoute = navController
        .currentBackStackEntryAsState().value?.destination?.route

    Column(modifier) {
        HorizontalDivider(
            color = MaterialTheme.colorScheme.outline,
            thickness = 1.dp,
            modifier = Modifier.fillMaxWidth()
        )

        NavigationBar(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier.fillMaxWidth()
        ) {
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = null
                    )
                },
                label = {
                    Text(
                        stringResource(R.string.bottom_navigation_inicio),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                selected = currentRoute == AppScreens.HomeScreen.route,
                onClick = {
                    if (currentRoute != AppScreens.HomeScreen.route) {
                        navController.navigate(AppScreens.HomeScreen.route) {
                            launchSingleTop = true
                        }
                    }
                }
            )

            NavigationBarItem(
                icon = {
                    Icon(Icons.Default.CalendarMonth, contentDescription = null)
                },
                label = {
                    Text(
                        stringResource(R.string.reservas),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                selected = currentRoute == AppScreens.ReservasScreen.route,
                onClick = {
                    if (currentRoute != AppScreens.ReservasScreen.route) {
                        navController.navigate(AppScreens.ReservasScreen.route) {
                            launchSingleTop = true
                        }
                    }
                }
            )

            NavigationBarItem(
                icon = {
                    Icon(Icons.Default.Favorite, contentDescription = null)
                },
                label = {
                    Text(
                        stringResource(R.string.favoritos),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                selected = currentRoute == AppScreens.FavoritosScreen.route,
                onClick = {
                    if (currentRoute != AppScreens.FavoritosScreen.route) {
                        navController.navigate(AppScreens.FavoritosScreen.route) {
                            launchSingleTop = true
                        }
                    }
                }
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun BottomNavigationPreview() {
    Practica3BTheme { BottomNavigation(rememberNavController()) }
}

@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun BottomNavigationDarkPreview() {
    Practica3BTheme { BottomNavigation(rememberNavController()) }
}

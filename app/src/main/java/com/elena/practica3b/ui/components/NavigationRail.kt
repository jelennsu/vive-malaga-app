package com.elena.practica3b.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.elena.practica3b.R
import com.elena.practica3b.navigation.AppScreens
import com.elena.practica3b.ui.theme.Practica3BTheme
import androidx.compose.material.icons.filled.Favorite

// Barra de navegación vertical con íconos y etiquetas para navegar entre pantallas.
@Composable
fun NavigationRail(navController: NavController, modifier: Modifier = Modifier) {
    val currentRoute = navController
        .currentBackStackEntryAsState().value?.destination?.route

    NavigationRail(
        modifier = modifier.fillMaxHeight(),
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
    ) {
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            NavigationRailItem(
                icon = {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = stringResource(R.string.bottom_navigation_inicio)
                    )
                },
                label = {
                    Text(stringResource(R.string.bottom_navigation_inicio))
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

            NavigationRailItem(
                icon = {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = stringResource(R.string.reservas)
                    )
                },
                label = {
                    Text(stringResource(R.string.reservas))
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

            NavigationRailItem(
                icon = {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = stringResource(R.string.favoritos)
                    )
                },
                label = {
                    Text(stringResource(R.string.favoritos))
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

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}


@Preview(showBackground = true, backgroundColor = 0xFFF5F0EE)
@Composable
fun NavigationRailPreview() {
    Practica3BTheme {
        NavigationRail(rememberNavController())
    }
}


package com.elena.practica3b.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.elena.practica3b.R
import com.elena.practica3b.ui.components.AppTopBar
import com.elena.practica3b.ui.components.BottomNavigation
import com.elena.practica3b.ui.components.EventDialog
import com.elena.practica3b.ui.components.NavigationRail
import com.elena.practica3b.ui.screens.categoria.CategoriaScreen
import com.elena.practica3b.ui.screens.drawer.DrawerScreen
import com.elena.practica3b.ui.screens.editprofile.EditProfileScreen
import com.elena.practica3b.ui.screens.favoritos.FavoritosScreen
import com.elena.practica3b.ui.screens.favoritos.FavoritosViewModel
import com.elena.practica3b.ui.screens.historial.HistorialScreen
import com.elena.practica3b.ui.screens.home.HomeScreen
import com.elena.practica3b.ui.screens.login.LoginScreen
import com.elena.practica3b.ui.screens.login.LoginViewModel
import com.elena.practica3b.ui.screens.lugar.LugarScreen
import com.elena.practica3b.ui.screens.registration.RegisterViewModel
import com.elena.practica3b.ui.screens.registration.RegistrationScreen
import com.elena.practica3b.ui.screens.reservas.ReservasScreen
import com.elena.practica3b.ui.screens.reservas.ReservasViewModel
import com.elena.practica3b.ui.screens.splash.SplashScreen
import com.elena.practica3b.ui.theme.Practica3BTheme
import kotlinx.coroutines.launch
import com.elena.practica3b.utils.sinDiacriticos


// Configura la navegación de la aplicación, definiendo las rutas
// y las animaciones de transición entre pantallas.
@ExperimentalAnimationApi
@Composable
fun AppNavigationPortrait(navController: NavHostController) {
    val excludedRoutesForTopBar = listOf(
        AppScreens.SplashScreen.route,
        AppScreens.LoginScreen.route,
        AppScreens.RegistrationScreen.route
    )

    val excludedRoutesForBottomBar = excludedRoutesForTopBar + listOf(AppScreens.LugarScreen.baseRoute)

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: ""
    val currentBaseRoute = getBaseRoute(currentRoute)
    val showTopBar = currentBaseRoute.isNotEmpty() && currentBaseRoute !in excludedRoutesForTopBar
    val showBottomBar = currentBaseRoute.isNotEmpty() && currentBaseRoute !in excludedRoutesForBottomBar
    
    val title = when (currentRoute) {
        AppScreens.HomeScreen.route -> stringResource(R.string.app_name)
        AppScreens.ReservasScreen.route -> stringResource(R.string.reservas)
        AppScreens.FavoritosScreen.route -> stringResource(R.string.favoritos)
        else -> stringResource(R.string.app_name)
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerScreen(
                onCloseDrawer = { scope.launch { drawerState.close() } },
                navController = navController
            )
        },
        scrimColor = Color.Black.copy(alpha = 0.5f)
    )  {
        Scaffold(
            topBar = {
                if (showTopBar) {
                    AppTopBar(
                        titleText = title,
                        onMenuClick = {
                            scope.launch {
                                if (drawerState.isClosed) drawerState.open() else drawerState.close()
                            }
                        }
                    )
                }
            },
            bottomBar = {
                if (showBottomBar) {
                    BottomNavigation(navController)
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = AppScreens.SplashScreen.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                addSplash(navController)
                addLogin(navController)
                addRegister(navController)
                addHome(navController)
                addReservas(navController)
                addFavoritos(navController)
                addLugar(navController)
                addEditProfile(navController)
                addCategoria(navController)
                addLocalidad(navController)
                addHistorial(navController)
            }
        }
    }
}



@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNavigationLandscape(navController: NavHostController) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: ""
    val currentBaseRoute = getBaseRoute(currentRoute)

    val excludedRoutesForTopBar = listOf(
        AppScreens.SplashScreen.route,
        AppScreens.LoginScreen.route,
        AppScreens.RegistrationScreen.route
    )

    val excludedRoutesForNavigationRail = excludedRoutesForTopBar + listOf(AppScreens.LugarScreen.baseRoute)

    val showTopBar = currentBaseRoute.isNotEmpty() && currentBaseRoute !in excludedRoutesForTopBar
    val showNavigationRail = currentBaseRoute.isNotEmpty() && currentBaseRoute !in excludedRoutesForNavigationRail

    val title = when (currentRoute) {
        AppScreens.HomeScreen.route -> stringResource(R.string.app_name)
        AppScreens.ReservasScreen.route -> stringResource(R.string.reservas)
        AppScreens.FavoritosScreen.route -> stringResource(R.string.favoritos)
        else -> stringResource(R.string.app_name)
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerScreen(
                onCloseDrawer = { scope.launch { drawerState.close() } },
                navController = navController
            )
        },
        scrimColor = Color.Black.copy(alpha = 0.5f)
    ) {
        Scaffold(
            topBar = {
                if (showTopBar) {
                    AppTopBar(
                        titleText = title,
                        onMenuClick = {
                            scope.launch {
                                if (drawerState.isClosed) drawerState.open() else drawerState.close()
                            }
                        }
                    )
                }
            }
        ) { innerPadding ->
            Row(modifier = Modifier.padding(innerPadding)) {
                if (showNavigationRail) {
                    NavigationRail(navController = navController)
                }

                NavHost(
                    navController = navController,
                    startDestination = AppScreens.SplashScreen.route,
                    modifier = Modifier.weight(1f)
                ) {
                    addSplash(navController)
                    addLogin(navController)
                    addRegister(navController)
                    addHome(navController)
                    addReservas(navController)
                    addFavoritos(navController)
                    addLugar(navController)
                    addEditProfile(navController)
                    addCategoria(navController)
                    addLocalidad(navController)
                    addHistorial(navController)
                }
            }
        }
    }
}


@ExperimentalAnimationApi
fun NavGraphBuilder.addLogin(
    navController: NavHostController
){
    composable(
        route = AppScreens.LoginScreen.route,
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { 1000 },
                animationSpec = tween(500)
            )
        },
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { -1000 },
                animationSpec = tween(500)
            )
        },
        popEnterTransition = {
            slideInHorizontally(
                initialOffsetX = { -1000 },
                animationSpec = tween(500)
            )
        },
        popExitTransition = {
            slideOutHorizontally(
                targetOffsetX = { 1000 },
                animationSpec = tween(500)
            )
        }
    ){
        val viewModel: LoginViewModel = hiltViewModel()

        if(viewModel.state.value.successLogin){
            EventDialog(message = R.string.success_login, isError = false, onDismiss = {
                navController.navigate("home_screen") {

                    popUpTo("login_screen") { inclusive = true }
                }
                viewModel.hideDialog()
            })
        } else {
            LoginScreen(
                onNavigateToRegister = {
                    navController.navigate(AppScreens.RegistrationScreen.route)
                },
                onDismissDialog = viewModel::hideDialog
            )
        }
    }
}

@ExperimentalAnimationApi
fun NavGraphBuilder.addRegister(
    navController: NavHostController
){
    composable(
        route = AppScreens.RegistrationScreen.route,
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { 1000 },
                animationSpec = tween(500)
            )
        },
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { -1000 },
                animationSpec = tween(500)
            )
        },
        popEnterTransition = {
            slideInHorizontally(
                initialOffsetX = { -1000 },
                animationSpec = tween(500)
            )
        },
        popExitTransition = {
            slideOutHorizontally(
                targetOffsetX = { 1000 },
                animationSpec = tween(500)
            )
        }
    ){
        val viewModel: RegisterViewModel = hiltViewModel()

        RegistrationScreen(
            navController = navController,
            viewModel = viewModel
        )
    }
}

@ExperimentalAnimationApi
fun NavGraphBuilder.addHome(navController: NavHostController) {
    composable(
        route = AppScreens.HomeScreen.route
    ){
        HomeScreen(navController = navController)
    }
}

@ExperimentalAnimationApi
fun NavGraphBuilder.addReservas(navController: NavHostController) {
    composable(
        route = AppScreens.ReservasScreen.route
    ){
        val viewModel: ReservasViewModel = hiltViewModel()
        ReservasScreen(viewModel)
    }
}

@ExperimentalAnimationApi
fun NavGraphBuilder.addFavoritos(navController: NavHostController) {
    composable(
        route = AppScreens.FavoritosScreen.route
    ){
        val viewModel: FavoritosViewModel = hiltViewModel()
        FavoritosScreen(navController,viewModel)
    }
}

@ExperimentalAnimationApi
fun NavGraphBuilder.addSplash(navController: NavHostController) {
    composable(
        route = AppScreens.SplashScreen.route
    ){
       SplashScreen(navController)
    }
}

fun NavGraphBuilder.addEditProfile(navController: NavHostController) {
    composable(
        route = AppScreens.EditProfileScreen.route
    ) {
        EditProfileScreen(
            navController = navController
        )
    }
}

fun NavGraphBuilder.addLugar(navController: NavHostController) {
    composable(
        route = "lugar_screen/{lugarId}",
        arguments = listOf(navArgument("lugarId") { type = NavType.StringType })
    ) { backStackEntry ->
        val lugarId = backStackEntry.arguments?.getString("lugarId") ?: ""
        LugarScreen(
            lugarId = lugarId,
            onBack = { navController.popBackStack() }
        )
    }
}

fun NavGraphBuilder.addCategoria(navController: NavHostController) {
    composable("categoria_screen/{categoria_nombre}") { backStackEntry ->
        val categoria = backStackEntry.arguments?.getString("categoria_nombre") ?: ""
        CategoriaScreen(
            titulo = categoria,
            navController = navController,
            filtro = { lugar ->
                lugar.categoria.trim().lowercase().sinDiacriticos() ==
                        categoria.replace("_", " ").lowercase().sinDiacriticos()
            }
        )
    }
}

fun NavGraphBuilder.addLocalidad(navController: NavHostController) {
    composable(
        route = AppScreens.LocalidadScreen.route,
        arguments = listOf(navArgument("localidad") { type = NavType.StringType })
    ) { backStackEntry ->
        val localidad = backStackEntry.arguments?.getString("localidad") ?: ""
        CategoriaScreen(
            titulo = localidad,
            navController = navController,
            filtro = { lugar ->
                lugar.localidad.trim().lowercase().sinDiacriticos() ==
                        localidad.replace("_", " ").lowercase().sinDiacriticos()
            }
        )
    }
}


fun NavGraphBuilder.addHistorial(navController: NavHostController) {
    composable(route = AppScreens.HistorialScreen.route) {
        HistorialScreen(
            onBack = { navController.popBackStack() },
            onLugarClicked = { lugarId ->
                navController.navigate(AppScreens.LugarScreen.createRoute(lugarId))
            }
        )
    }
}








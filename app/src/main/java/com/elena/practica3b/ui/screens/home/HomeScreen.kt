package com.elena.practica3b.ui.screens.home

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.elena.practica3b.R
import com.elena.practica3b.navigation.AppScreens
import com.elena.practica3b.ui.screens.lugar.data.Lugar
import com.elena.practica3b.ui.theme.Practica3BTheme

// HomeScreen: Componente principal de la pantalla de inicio que incluye una barra de búsqueda
// y dos secciones: Lugares y Actividades.
// HomeSection: Componente reutilizable para mostrar secciones con título y contenido dinámico.

@Composable
fun HomeSection(
    @StringRes title: Int,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(modifier) {
        Text(
            text = stringResource(title),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .paddingFromBaseline(top = 40.dp, bottom = 16.dp)
                .padding(horizontal = 16.dp)
        )
        content()
    }
}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val lugaresFiltrados by homeViewModel.lugaresFiltrados.collectAsState()
    val searchQuery by homeViewModel.searchQuery.collectAsState()
    val suggestions by homeViewModel.sugerencias.collectAsState()

    HomeScreenContent(
        navController = navController,
        lugaresFiltrados = lugaresFiltrados,
        searchQuery = searchQuery,
        suggestions = suggestions,
        onSearchQueryChange = homeViewModel::updateSearchQuery,
        onSuggestionClicked = { lugar ->
            navController.navigate(AppScreens.LugarScreen.createRoute(lugar.id))
        },
        onSearchSubmit = {
            val query = searchQuery.trim()
            if (homeViewModel.esCategoriaValida(query)) {
                val categoriaParam = query.replace(" ", "_")
                navController.navigate(AppScreens.CategoriaScreen.createRoute(categoriaParam))
            } else {
                val lugar = lugaresFiltrados.firstOrNull()
                lugar?.let {
                    navController.navigate(AppScreens.LugarScreen.createRoute(it.id))
                }
            }
        },
        onLocalidadClicked = { localidad ->
            val localidadParam = localidad.replace(" ", "_")
            navController.navigate(AppScreens.LocalidadScreen.createRoute(localidadParam))
        },
        onCategoriaClicked = { categoria ->
            val categoriaParam = categoria.replace(" ", "_")
            navController.navigate(AppScreens.CategoriaScreen.createRoute(categoriaParam))
        },
        modifier = modifier
    )
}


@Composable
fun HomeScreenContent(
    navController: NavController,
    lugaresFiltrados: List<Lugar>,
    searchQuery: String,
    suggestions: List<SugerenciaBusqueda>,
    onSearchQueryChange: (String) -> Unit,
    onSuggestionClicked: (Lugar) -> Unit,
    onSearchSubmit: () -> Unit,
    onLocalidadClicked: (String) -> Unit,
    onCategoriaClicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .imePadding(),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            SearchBar(
                modifier = Modifier.padding(horizontal = 16.dp),
                searchQuery = searchQuery,
                onSearchQueryChange = onSearchQueryChange,
                suggestions = suggestions,
                onLugarClicked = onSuggestionClicked,
                onCategoriaClicked = onCategoriaClicked,
                onLocalidadClicked = onLocalidadClicked,
                onSearchSubmit = onSearchSubmit
            )
        }

        item {
            HomeSection(title = R.string.lugares) {
                LugaresRow(
                    navController = navController,
                    lugaresFirebase = lugaresFiltrados
                )
            }
        }

        item {
            HomeSection(title = R.string.categorias) {
                ActividadesGrid(
                    modifier = Modifier.heightIn(max = 400.dp),
                    onCategoriaClick = { categoriaTexto ->
                        val categoriaParam = categoriaTexto.replace(" ", "_")
                        navController.navigate(AppScreens.CategoriaScreen.createRoute(categoriaParam))
                    }
                )
            }
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }
    }
}







@Preview(showBackground = true, backgroundColor = 0xFFF5F0EE)
@Composable
fun HomeSectionPreview() {
    val dummyLugares = listOf(
        Lugar(
            id = "1",
            nombre = "Alcazaba",
            descripcion = "Lugar histórico",
            imagenUrl = "",
            categoria = "Cultura",
            direccion = "Calle Alcazabilla"
        )
    )

    Practica3BTheme {
        HomeSection(R.string.lugares) {
            LugaresRow(
                navController = rememberNavController(),
                lugaresFirebase = dummyLugares
            )
        }
    }
}



@Preview(showBackground = true)
@Composable
fun ScreenContentPreview() {
    val dummyLugares = listOf(
        Lugar(
            id = "1",
            nombre = "Alcazaba",
            descripcion = "Lugar histórico",
            imagenUrl = "",
            categoria = "Cultura",
            direccion = "Calle Alcazabilla",
            localidad = "Málaga"
        )
    )

    val dummySuggestions = dummyLugares.map { SugerenciaBusqueda.PorLugar(it) }

    Practica3BTheme {
        HomeScreenContent(
            navController = rememberNavController(),
            lugaresFiltrados = dummyLugares,
            searchQuery = "",
            onSearchQueryChange = {},
            suggestions = dummySuggestions,
            onSuggestionClicked = {},
            onSearchSubmit = {},
            modifier = Modifier.fillMaxSize(),
            onCategoriaClicked = {},
            onLocalidadClicked = {}
        )
    }
}




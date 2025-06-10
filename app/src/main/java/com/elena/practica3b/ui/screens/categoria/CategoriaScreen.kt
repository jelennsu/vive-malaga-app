package com.elena.practica3b.ui.screens.categoria

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.elena.practica3b.navigation.AppScreens
import com.elena.practica3b.ui.screens.home.HomeViewModel
import com.elena.practica3b.ui.screens.lugar.data.Lugar
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.*
import java.text.Normalizer

@Composable
fun CategoriaScreen(
    titulo: String,
    navController: NavController,
    filtro: (Lugar) -> Boolean,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val lugares by viewModel.lugares.collectAsState()

    val lugaresFiltrados = lugares.filter(filtro)

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = titulo.replace("_", " "),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (lugaresFiltrados.isEmpty()) {
            Text("No hay lugares que coincidan con la selección.")
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(lugaresFiltrados) { lugar ->
                    LugarCard(
                        lugar = lugar,
                        onClick = {
                            navController.navigate(AppScreens.LugarScreen.createRoute(lugar.id))
                        }
                    )
                }
            }
        }
    }
}


@Composable
fun LugarCard(lugar: Lugar, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            AsyncImage(
                model = lugar.imagenUrl,
                contentDescription = lugar.nombre,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            )
            Column(modifier = Modifier.padding(8.dp)) {
                Text(text = lugar.nombre, style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}



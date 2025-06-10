package com.elena.practica3b.ui.screens.home

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.elena.practica3b.R
import com.elena.practica3b.data.lugaresData
import com.elena.practica3b.navigation.AppScreens
import com.elena.practica3b.ui.screens.lugar.data.Lugar
import com.elena.practica3b.ui.theme.Practica3BTheme
import androidx.compose.runtime.getValue
import androidx.navigation.compose.rememberNavController


// Muestra un ícono y texto centrado, usado en una fila horizontal.
@Composable
fun LugaresElement(
    @DrawableRes drawable: Int,
    @StringRes text: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(drawable),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(88.dp)
                .clip(CircleShape)
        )
        Text(
            text = stringResource(text),
            modifier = Modifier.paddingFromBaseline(top = 24.dp, bottom = 8.dp),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun LugaresRow(
    navController: NavController,
    lugaresFirebase: List<Lugar>
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        modifier = Modifier
    ) {
        items(lugaresData) { lugarLocal ->
            val nombreString = stringResource(id = lugarLocal.text)
            Column(
                modifier = Modifier
                    .clickable {
                        val lugarEncontrado = lugaresFirebase.firstOrNull {
                            it.nombre.equals(nombreString, ignoreCase = true)
                        }
                        if (lugarEncontrado == null) {
                            Log.e("NAV", "No se encontró lugar en Firebase para el nombre: $nombreString")
                        } else if (lugarEncontrado.id.isNotBlank()) {
                            navController.navigate("lugar_screen/${lugarEncontrado.id}")
                        } else {
                            Log.e("NAV", "El lugar encontrado no tiene id válido")
                        }
                    }
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = lugarLocal.drawable),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(88.dp)
                        .clip(CircleShape)
                )
                Text(
                    text = nombreString,
                    modifier = Modifier.paddingFromBaseline(top = 24.dp, bottom = 8.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun LugaresRowWithViewModel(
    navController: NavController,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val lugaresFirebase by homeViewModel.lugares.collectAsState()
    LugaresRow(navController = navController, lugaresFirebase = lugaresFirebase)
}






@Preview(showBackground = true, backgroundColor = 0xFFF5F0EE)
@Composable
fun LugaresElementPreview() {
    Practica3BTheme {
        LugaresElement(
            text = R.string.alcazaba,
            drawable = R.drawable.alcazaba,
            modifier = Modifier.padding(8.dp),
            onClick = {}
        )
    }
}
@Preview(showBackground = true, backgroundColor = 0xFFF5F0EE)
@Composable
fun LugaresRowPreview() {
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
        LugaresRow(
            navController = rememberNavController(),
            lugaresFirebase = dummyLugares
        )
    }
}

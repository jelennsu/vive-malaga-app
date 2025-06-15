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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.elena.practica3b.R
import com.elena.practica3b.ui.screens.lugar.Lugar
import com.elena.practica3b.ui.theme.Practica3BTheme


/*
 * Composable para mostrar un elemento visual con imagen circular y texto debajo,
 * que responde a clics mediante un callback.
 */
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

/*
 * Fila horizontal de elementos tipo "Lugar" local,
 * que al clicar busca el lugar correspondiente en Firebase
 * y navega a la pantalla de detalle usando NavController.
 * Registra errores si no encuentra el lugar o si el id es inválido.
 */
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

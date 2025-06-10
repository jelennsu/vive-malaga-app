package com.elena.practica3b.ui.screens.home

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.elena.practica3b.R
import com.elena.practica3b.data.actividadesData
import com.elena.practica3b.ui.theme.Practica3BTheme

// Componente ActividadesCard:
// Muestra una tarjeta con una imagen y un texto. La imagen se ajusta a la pantalla utilizando
// un `Image` con un `painterResource` para cargar la imagen desde los recursos.
// El texto es un `String` que se obtiene de los recursos, y se muestra debajo de la imagen.

// Componente ActividadesGrid:
// Muestra un conjunto de tarjetas `ActividadesCard` dispuestas en una cuadrícula de dos columnas.
// Utiliza `LazyVerticalGrid` para manejar una lista de elementos de forma eficiente,
// permitiendo el desplazamiento de la lista con un espaciado entre elementos tanto horizontal como vertical.

@Composable
fun ActividadesCard(
    @DrawableRes drawable: Int,
    @StringRes text: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = modifier.clickable { onClick() } // ← aquí el click
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(drawable),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(text),
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier
                    .padding(horizontal = 6.dp)
                    .padding(bottom = 8.dp)
            )
        }
    }
}


@Composable
fun ActividadesGrid(
    modifier: Modifier = Modifier,
    onCategoriaClick: (String) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.padding(horizontal = 16.dp)
    ) {
        items(actividadesData) { item ->
            val categoriaTexto = stringResource(id = item.text) // ✅ AQUÍ SÍ es composable
            ActividadesCard(
                drawable = item.drawable,
                text = item.text,
                onClick = {
                    onCategoriaClick(categoriaTexto)
                }
            )
        }
    }
}







@Preview(showBackground = true, backgroundColor = 0xFFF5F0EE)
@Composable
fun ActividadesCardPreview() {
    Practica3BTheme {
        ActividadesCard(
            text = R.string.gastronomia,
            drawable = R.drawable.gastronomia,
            modifier = Modifier.padding(8.dp),
            onClick = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F0EE)
@Composable
fun ActividadesGridPreview() {
    Practica3BTheme { ActividadesGrid(
        onCategoriaClick = {  }
    ) }
}

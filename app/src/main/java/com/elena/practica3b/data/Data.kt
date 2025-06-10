package com.elena.practica3b.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.elena.practica3b.R

// Define listas de lugares y actividades en Málaga con sus respectivos recursos de imagen y texto.
// Se utiliza la clase `DrawableStringPair` para almacenar pares de referencias a imágenes y textos.

data class DrawableStringPair(
    @DrawableRes val drawable: Int,
    @StringRes val text: Int
)

val lugaresData = listOf(
    R.drawable.alcazaba to R.string.alcazaba,
    R.drawable.teatro_romano to R.string.teatro_romano,
    R.drawable.museo_picasso to R.string.museo_picasso,
    R.drawable.caminito_rey to R.string.caminito_rey,
    R.drawable.playa to R.string.malagueta,
    R.drawable.manquita to R.string.catedral
).map { DrawableStringPair(it.first, it.second) }

val actividadesData = listOf(
    R.drawable.cultura to R.string.cultura,
    R.drawable.gastronomia to R.string.gastronomia,
    R.drawable.naturaleza to R.string.naturaleza,
    R.drawable.aventura to R.string.aventura,
    R.drawable.ocio_entretenimiento to R.string.ocio,
    R.drawable.familia_ninos to R.string.familia
).map { DrawableStringPair(it.first, it.second) }





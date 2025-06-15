package com.elena.practica3b.utils

import java.text.Normalizer

/**
 * Extensión de String que elimina los diacríticos (tildes y otros signos diacríticos)
 * para facilitar comparaciones o búsquedas insensibles a acentos.
 *
 * Ejemplo: "áéíóú" -> "aeiou"
 *
 * @return Una nueva cadena sin signos diacríticos.
 */
fun String.sinDiacriticos(): String {
    val normalized = Normalizer.normalize(this, Normalizer.Form.NFD)
    return normalized.replace("\\p{M}+".toRegex(), "")
}

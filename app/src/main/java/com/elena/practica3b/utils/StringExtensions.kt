package com.elena.practica3b.utils

import java.text.Normalizer

fun String.sinDiacriticos(): String {
    val normalized = Normalizer.normalize(this, Normalizer.Form.NFD)
    return normalized.replace("\\p{M}+".toRegex(), "")
}

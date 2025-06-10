package com.elena.practica3b.model

/**
 * Clase sellada que representa el estado de una operación asíncrona.
 *
 * @param T Tipo de dato que se espera en caso de éxito.
 */
sealed class Response<out T> {

    /**
     * Indica que la operación está en proceso de ejecución.
     */
    object Loading : Response<Nothing>()

    /**
     * Representa el resultado exitoso de una operación.
     *
     * @param T Tipo del dato obtenido.
     * @property data Datos resultantes de la operación.
     */
    data class Success<out T>(val data: T) : Response<T>()

    /**
     * Representa un fallo en la operación.
     *
     * @property e Excepción que describe el error ocurrido.
     */
    data class Failure(val e: Exception) : Response<Nothing>()
}

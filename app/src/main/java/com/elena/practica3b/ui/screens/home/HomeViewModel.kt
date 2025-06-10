package com.elena.practica3b.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elena.practica3b.ui.screens.lugar.data.Lugar
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import com.elena.practica3b.utils.sinDiacriticos
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private val _lugares = MutableStateFlow<List<Lugar>>(emptyList())
    val lugares: StateFlow<List<Lugar>> = _lugares

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _categoriaSeleccionada = MutableStateFlow<String?>(null)
    private val _localidadSeleccionada = MutableStateFlow<String?>(null)

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _sugerencias = MutableStateFlow<List<SugerenciaBusqueda>>(emptyList())
    val sugerencias: StateFlow<List<SugerenciaBusqueda>> = _sugerencias

    private val categoriasValidas = listOf(
        "Cultura", "Gastronomía", "Naturaleza", "Aventura", "Ocio", "Familia"
    )

    init {
        loadLugares()

        // Sugerencias
        viewModelScope.launch {
            combine(_lugares, _searchQuery) { lugares, query ->
                obtenerSugerencias(lugares, query)
            }.collect { _sugerencias.value = it }
        }
    }

    val lugaresFiltrados: StateFlow<List<Lugar>> = combine(
        _lugares, _searchQuery, _categoriaSeleccionada, _localidadSeleccionada
    ) { lugares, query, categoria, localidad ->
        val texto = query.trim().lowercase().sinDiacriticos()
        lugares.filter { lugar ->
            val nombre = lugar.nombre.lowercase().sinDiacriticos()
            val cat = lugar.categoria.lowercase().sinDiacriticos()
            val loc = lugar.localidad.orEmpty().lowercase().sinDiacriticos()

            (texto.isBlank() || nombre.contains(texto) || cat.contains(texto) || loc.contains(texto)) &&
                    (categoria == null || lugar.categoria.equals(categoria, ignoreCase = true)) &&
                    (localidad == null || lugar.localidad.equals(localidad, ignoreCase = true))
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private fun loadLugares() {
        _isLoading.value = true
        firestore.collection("lugares")
            .get()
            .addOnSuccessListener { result ->
                _lugares.value = result.mapNotNull { it.toObject(Lugar::class.java).copy(id = it.id) }
                _isLoading.value = false
            }
            .addOnFailureListener {
                _error.value = it.message
                _isLoading.value = false
            }
    }

    fun updateSearchQuery(newQuery: String) {
        _searchQuery.value = newQuery
    }


    fun esCategoriaValida(texto: String): Boolean {
        val limpio = texto.trim().lowercase().sinDiacriticos()
        return categoriasValidas.any { it.lowercase().sinDiacriticos() == limpio }
    }

    private fun obtenerSugerencias(lugares: List<Lugar>, query: String): List<SugerenciaBusqueda> {
        val texto = query.trim().lowercase().sinDiacriticos()
        if (texto.isBlank()) return emptyList()

        val sugerencias = mutableListOf<SugerenciaBusqueda>()

        // Categorías coincidentes
        sugerencias += categoriasValidas
            .filter { it.lowercase().sinDiacriticos().contains(texto) }
            .map { SugerenciaBusqueda.PorCategoria(it) }

        // Localidades únicas coincidentes
        sugerencias += lugares.mapNotNull { it.localidad }
            .distinctBy { it.lowercase().sinDiacriticos() }
            .filter { it.lowercase().sinDiacriticos().contains(texto) }
            .map { SugerenciaBusqueda.PorLocalidad(it) }

        // Lugares por nombre o localidad
        sugerencias += lugares.filter {
            it.nombre.lowercase().sinDiacriticos().contains(texto) ||
                    it.localidad.orEmpty().lowercase().sinDiacriticos().contains(texto)
        }.map { SugerenciaBusqueda.PorLugar(it) }

        return sugerencias
    }


}











package com.elena.practica3b.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.elena.practica3b.ui.screens.lugar.data.Lugar
import com.elena.practica3b.ui.theme.Practica3BTheme


@Composable
fun SearchBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    suggestions: List<SugerenciaBusqueda>,
    onLugarClicked: (Lugar) -> Unit,
    onCategoriaClicked: (String) -> Unit,
    onLocalidadClicked: (String) -> Unit,
    onSearchSubmit: () -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    var showSuggestions by remember { mutableStateOf(false) }

    Column(modifier) {
        TextField(
            value = searchQuery,
            onValueChange = {
                onSearchQueryChange(it)
                showSuggestions = it.isNotBlank() && suggestions.isNotEmpty()
            },
            modifier = Modifier
                .fillMaxWidth(),
            placeholder = { Text("Buscar...") },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    onSearchSubmit()
                    focusManager.clearFocus()
                    showSuggestions = false
                }
            )
        )

        if (showSuggestions) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 200.dp) // altura máxima para lista sugerencias
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                items(suggestions.take(5)) { sugerencia ->
                    val text = when (sugerencia) {
                        is SugerenciaBusqueda.PorLugar -> sugerencia.lugar.nombre
                        is SugerenciaBusqueda.PorCategoria -> "Buscar por categoría: ${sugerencia.categoria}"
                        is SugerenciaBusqueda.PorLocalidad -> "Buscar por localidad: ${sugerencia.localidad}"
                    }

                    Text(
                        text = text,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                when (sugerencia) {
                                    is SugerenciaBusqueda.PorLugar -> onLugarClicked(sugerencia.lugar)
                                    is SugerenciaBusqueda.PorCategoria -> onCategoriaClicked(sugerencia.categoria)
                                    is SugerenciaBusqueda.PorLocalidad -> onLocalidadClicked(sugerencia.localidad)
                                }
                                showSuggestions = false
                                // El foco puede seguir, así teclado no se oculta
                            }
                            .padding(16.dp)
                    )
                    HorizontalDivider()
                }
            }
        }
    }
}






@Preview(showBackground = true, backgroundColor = 0xFFF5F0EE)
@Composable
fun SearchBarPreview() {
    Practica3BTheme {  }
}
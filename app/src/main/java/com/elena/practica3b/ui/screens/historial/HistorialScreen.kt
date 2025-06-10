package com.elena.practica3b.ui.screens.historial

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.elena.practica3b.data.local.entity.Historial
import com.elena.practica3b.ui.components.ConfirmActionDialog
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue


@Composable
fun HistorialScreen(
    viewModel: HistorialViewModel = hiltViewModel(),
    onLugarClicked: (String) -> Unit,
    onBack: () -> Unit
) {
    val historial by viewModel.historial.collectAsState()

    HistorialContent(
        historial = historial,
        onBack = onBack,
        onLugarClicked = onLugarClicked,
        onBorrarHistorial = { viewModel.borrarHistorial() }
    )
}

@Composable
fun HistorialContent(
    historial: List<Historial>,
    onLugarClicked: (String) -> Unit,
    onBack: () -> Unit,
    onBorrarHistorial: () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
            }

            Text(
                text = "Historial de visitas",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(start = 4.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            if (historial.isNotEmpty()) {
                IconButton(onClick = { showDialog = true }) {
                    Icon(Icons.Default.Delete, contentDescription = "Borrar historial")
                }
            }
        }

        if (historial.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No has visitado ningún lugar todavía.")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                items(historial) { item ->
                    HistorialItem(
                        historial = item,
                        onClick = { onLugarClicked(item.lugarId) }
                    )
                    HorizontalDivider()
                }
            }
        }

        // Usamos el diálogo reutilizable
        if (showDialog) {
            ConfirmActionDialog(
                title = "¿Borrar historial?",
                message = "¿Estás seguro de que deseas borrar todo tu historial de visitas? Esta acción no se puede deshacer.",
                onConfirm = {
                    showDialog = false
                    onBorrarHistorial()
                },
                onDismiss = {
                    showDialog = false
                }
            )
        }
    }
}


@Composable
fun HistorialItem(
    historial: Historial,
    onClick: () -> Unit
) {
    val fecha = remember(historial.timestamp) {
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        sdf.format(Date(historial.timestamp))
    }

    ListItem(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp),
        headlineContent = { Text(historial.nombre) },
        supportingContent = { Text(fecha) },
    )
}



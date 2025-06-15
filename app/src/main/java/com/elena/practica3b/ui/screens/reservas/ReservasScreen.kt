package com.elena.practica3b.ui.screens.reservas


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.text.font.FontWeight
import java.text.SimpleDateFormat
import java.util.*
import com.elena.practica3b.ui.components.ConfirmActionDialog

/**
 * Composable que muestra la lista de reservas del usuario en forma de tarjetas.
 * Cada reserva muestra el nombre del lugar, cantidad de plazas y la fecha/hora reservada.
 *
 * Permite cancelar una reserva mediante un botón que abre un diálogo de confirmación.
 * Al confirmar, se notifica al ViewModel para eliminar la reserva de la fuente de datos.
 *
 * La lista se presenta en un LazyColumn con separación entre elementos y estilos coherentes con el tema.
 *
 * @param viewModel Instancia de ReservasViewModel que provee las reservas y la lógica para cancelar.
 */

@Composable
fun ReservasScreen(
    viewModel: ReservasViewModel = hiltViewModel()
) {
    val reservas by remember { derivedStateOf { viewModel.reservas } }
    var reservaAEliminar by remember { mutableStateOf<Reserva?>(null) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(reservas) { reserva ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = reserva.nombreLugar,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        "Cantidad: ${reserva.cantidad}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        "Fecha y hora: ${
                            SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                                .format(Date(reserva.fechaHora))
                        }",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp)) // Separación vertical

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            onClick = { reservaAEliminar = reserva },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.error,
                                contentColor = MaterialTheme.colorScheme.onError
                            ),
                            shape = RoundedCornerShape(20.dp),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Text("Cancelar")
                        }
                    }

                }
            }

        }

    }

    reservaAEliminar?.let { reserva ->
        ConfirmActionDialog(
            title = "Cancelar reserva",
            message = "¿Estás seguro de que deseas cancelar esta reserva?",
            onConfirm = {
                viewModel.cancelarReserva(reserva)
                reservaAEliminar = null
            },
            onDismiss = {
                reservaAEliminar = null
            }
        )
    }
}




@Preview(showBackground = true)
@Composable
fun GaleriaScreenPreview() {
    ReservasScreen()
}

package com.elena.practica3b.ui.screens.lugar

import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.elena.practica3b.ui.screens.lugar.data.Lugar
import com.elena.practica3b.ui.theme.Practica3BTheme
import androidx.compose.ui.text.style.TextAlign
import androidx.core.net.toUri
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.Calendar
import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.elena.practica3b.ui.screens.lugar.data.Review
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.Surface
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.rememberCoroutineScope
import com.elena.practica3b.ui.screens.favoritos.Favorito
import com.elena.practica3b.ui.screens.favoritos.FavoritosViewModel
import com.elena.practica3b.ui.screens.historial.HistorialViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

@Composable
fun LugarScreen(
    lugarId: String,
    lugarViewModel: LugarViewModel = hiltViewModel(),
    historialViewModel: HistorialViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val state by lugarViewModel.state.collectAsState()
    var showDateTimePicker by remember { mutableStateOf(false) }
    var reviewText by remember { mutableStateOf("") }
    val favoritosViewModel: FavoritosViewModel = hiltViewModel()
    val favoritos by favoritosViewModel.favoritos.collectAsState()
    val isFavorito = favoritos.any { it.lugarId == lugarId }

    var cantidadSeleccionada by remember { mutableIntStateOf(0) }
    val coroutineScope = rememberCoroutineScope()

    var reviewSendResult by remember { mutableStateOf<Boolean?>(null) }

    LaunchedEffect(lugarId) {
        lugarViewModel.loadLugar(lugarId)
        lugarViewModel.loadReviews(lugarId)
    }

    LaunchedEffect(state.lugar) {
        state.lugar?.let { lugar ->
            historialViewModel.guardarEnHistorial(lugar)
        }
    }

    // Mostrar mensaje tras enviar reseña
    LaunchedEffect(reviewSendResult) {
        reviewSendResult?.let { success ->
            Toast.makeText(
                context,
                if (success) "Reseña enviada con éxito" else "Error al enviar reseña",
                Toast.LENGTH_SHORT
            ).show()
            if (success) reviewText = ""  // limpiar campo solo si éxito
            reviewSendResult = null
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            state.isLoading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            state.errorMessage != null -> Text(
                text = state.errorMessage!!,
                color = Color.Red,
                modifier = Modifier.align(Alignment.Center)
            )
            state.lugar != null -> {
                LugarContent(
                    lugar = state.lugar!!,
                    reviews = state.reviews,
                    reviewText = reviewText,
                    onReviewTextChange = { reviewText = it },
                    onReviewSend = {
                        if (reviewText.isBlank()) {
                            Toast.makeText(context, "La reseña no puede estar vacía", Toast.LENGTH_SHORT).show()
                            return@LugarContent
                        }
                        lugarViewModel.addReview(reviewText, lugarId) { success ->
                            reviewSendResult = success
                        }
                    },
                    onBack = onBack,
                    onReservar = { cantidad ->
                        cantidadSeleccionada = cantidad
                        showDateTimePicker = true
                    },
                    onDeleteReview = { reviewId ->
                        lugarViewModel.deleteReview(reviewId, lugarId) { success, errorMsg ->
                            Toast.makeText(
                                context,
                                if (success) "Reseña eliminada" else "Error: $errorMsg",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    isFavorito = isFavorito,
                    onToggleFavorito = { lugar ->
                        val favorito = Favorito(
                            usuarioId = FirebaseAuth.getInstance().currentUser?.uid.orEmpty(),
                            lugarId = lugar.id,
                            nombre = lugar.nombre,
                            imagenUrl = lugar.imagenUrl
                        )
                        coroutineScope.launch {
                            if (isFavorito) {
                                favoritosViewModel.removeFavorito(favorito)
                            } else {
                                favoritosViewModel.addFavorito(favorito)
                            }
                        }
                    }
                )
            }
        }

        if (showDateTimePicker && state.lugar != null) {
            DateTimePickerDialog(
                onDismiss = { showDateTimePicker = false },
                onDateTimeSelected = { fechaHora ->
                    showDateTimePicker = false
                    lugarViewModel.hacerReserva(lugarId, fechaHora, cantidadSeleccionada) { success, message ->
                        Toast.makeText(
                            context,
                            if (success) "Reserva realizada con éxito" else "Error: $message",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            )
        }
    }
}


@Composable
fun LugarContent(
    lugar: Lugar,
    reviews: List<Review>,
    reviewText: String,
    onReviewTextChange: (String) -> Unit,
    onReviewSend: () -> Unit,
    onBack: () -> Unit,
    onReservar: (Int) -> Unit,
    onDeleteReview: (String) -> Unit,
    isFavorito: Boolean,
    onToggleFavorito: (Lugar) -> Unit
) {
    val context = LocalContext.current
    var cantidadText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Header con back y título
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver"
                )
            }
            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = lugar.nombre,
                style = MaterialTheme.typography.headlineMedium,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f) // <- ocupa todo el espacio entre back y favorito
            )

            IconButton(onClick = { onToggleFavorito(lugar) }) {
                Icon(
                    imageVector = if (isFavorito) Icons.Default.Star else Icons.Default.StarBorder,
                    contentDescription = "Favorito",
                    tint = if (isFavorito) Color(0xFFFFC107) else Color.Gray
                )
            }
        }


        Spacer(Modifier.height(8.dp))

        // Imagen principal
        AsyncImage(
            model = lugar.imagenUrl,
            contentDescription = "Imagen de ${lugar.nombre}",
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .clip(MaterialTheme.shapes.medium),
            contentScale = ContentScale.Crop
        )

        Spacer(Modifier.height(16.dp))

        // Información básica
        Column(modifier = Modifier.padding(horizontal = 4.dp)) {
            Text(text = "Localidad: ${lugar.localidad}", style = MaterialTheme.typography.titleSmall)
            Spacer(Modifier.height(4.dp))
            Text(text = "Categoría: ${lugar.categoria}", style = MaterialTheme.typography.titleSmall)
            Spacer(Modifier.height(12.dp))
            Text(
                text = lugar.descripcion,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Justify,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(Modifier.height(20.dp))

        // Botones acción
        Button(
            onClick = { openMap(context, lugar.direccion) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(imageVector = Icons.Default.Map, contentDescription = "Abrir mapa")
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Abrir en Google Maps")
        }

        Spacer(Modifier.height(12.dp))

        HorizontalDivider()

        Spacer(Modifier.height(12.dp))

        Text(
            text = "Hacer reserva",
            style = MaterialTheme.typography.titleMedium
        )

        OutlinedTextField(
            value = cantidadText,
            onValueChange = { if (it.all { char -> char.isDigit() }) cantidadText = it },
            label = { Text("Cantidad") },
            placeholder = { Text("Número de personas") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                val cantidad = cantidadText.toIntOrNull()
                if (cantidad == null || cantidad <= 0) {
                    Toast.makeText(context, "Introduce una cantidad válida", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                onReservar(cantidad)
                cantidadText = ""  // Limpiar el campo tras reservar
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Reservar")
        }

        Spacer(Modifier.height(12.dp))

        HorizontalDivider()

        Spacer(Modifier.height(12.dp))

        Text(
            text = "Escribe tu reseña",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = reviewText,
            onValueChange = onReviewTextChange,
            placeholder = { Text("Escribe aquí tu opinión...") },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            maxLines = 5,
            singleLine = false,
            textStyle = MaterialTheme.typography.bodyMedium
        )

        Spacer(Modifier.height(12.dp))

        Button(
            onClick = onReviewSend,
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Enviar")
        }

        Spacer(Modifier.height(12.dp))

        HorizontalDivider()

        Spacer(Modifier.height(12.dp))

        Text(
            text = "Reseñas",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Column(modifier = Modifier.fillMaxWidth()) {
            reviews.forEach { review ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            // Avatar circular con inicial
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(
                                        color = MaterialTheme.colorScheme.primaryContainer,
                                        shape = CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = review.usuarioNombre.firstOrNull()?.uppercase() ?: "?",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Text(
                                text = review.usuarioNombre,
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                modifier = Modifier.weight(1f)
                            )

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.CalendarToday,
                                    contentDescription = "Fecha",
                                    modifier = Modifier.size(16.dp),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = review.fecha.formatToReadableDate(),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            if (review.canDelete) {
                                IconButton(
                                    onClick = { onDeleteReview(review.id) },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Eliminar reseña",
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        }

                        Spacer(Modifier.height(8.dp))

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.1f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = review.comentario,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(24.dp))
    }
}

// Función de extensión para formatear el timestamp a fecha legible
fun Long.formatToReadableDate(): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return sdf.format(Date(this))
}
@Composable
fun DateTimePickerDialog(
    onDismiss: () -> Unit,
    onDateTimeSelected: (Long) -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val currentYear = calendar.get(Calendar.YEAR)
    val currentMonth = calendar.get(Calendar.MONTH)
    val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

    var pickedDate by remember { mutableStateOf<LocalDate?>(null) }
    var showTimePicker by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        DatePickerDialog(context, { _, year, month, day ->
            pickedDate = LocalDate.of(year, month + 1, day)
            showTimePicker = true
        }, currentYear, currentMonth, currentDay).show()
    }

    if (showTimePicker && pickedDate != null) {
        TimePickerDialog(context, { _, hour, minute ->
            val pickedTime = LocalTime.of(hour, minute)
            val zoned = ZonedDateTime.of(pickedDate, pickedTime, ZoneId.systemDefault())
            onDateTimeSelected(zoned.toInstant().toEpochMilli())
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show()
    }
}

fun openMap(context: Context, address: String) {
    val uri = "geo:0,0?q=${Uri.encode(address)}".toUri()
    val intent = Intent(Intent.ACTION_VIEW, uri).apply {
        setPackage("com.google.android.apps.maps")
    }
    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent)
    } else {
        Toast.makeText(context, "No se encontró aplicación de mapas", Toast.LENGTH_SHORT).show()
    }
}

@Preview(showBackground = true)
@Composable
fun LugarContentPreview() {
    val lugarEjemplo = Lugar(
        id = "1",
        nombre = "La Alcazaba",
        categoria = "Cultura",
        localidad = "Málaga",
        descripcion = "La Alcazaba es una fortaleza palaciega construida por los musulmanes en el siglo XI.",
        direccion = "C/ Alcazabilla, 2, Distrito Centro, 29012 Málaga",
        imagenUrl = "https://firebasestorage.googleapis.com/v0/b/practica3b-5e5a8.firebasestorage.app/o/images%2Falcazaba.jpg?alt=media&token=7554ab10-f3d4-4258-94ef-9fd389b8b49a"
    )

    val reviewsEjemplo = listOf(
        Review(
            usuarioId = "Ana Pérez",
            comentario = "Lugar increíble, la experiencia fue fantástica y el personal muy amable."
        ),
        Review(
            usuarioId = "Carlos Ruiz",
            comentario = "Buena ubicación y ambiente agradable, aunque la espera fue un poco larga."
        )
    )

    Practica3BTheme {
        LugarContent(
            lugar = lugarEjemplo,
            onBack = {},
            onReservar = {},
            reviews = reviewsEjemplo,
            reviewText = "",
            onReviewTextChange = {},
            onReviewSend = {},
            onDeleteReview = {},
            isFavorito = false,
            onToggleFavorito = { }
        )
    }
}





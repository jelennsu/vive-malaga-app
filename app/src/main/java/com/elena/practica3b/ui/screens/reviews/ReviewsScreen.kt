package com.elena.practica3b.ui.screens.reviews

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.elena.practica3b.ui.components.AppTopBar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

// Pantalla de reseñas: Esta pantalla permite visualizar una lista de reseñas que los usuarios han
// dejado. El usuario puede agregar una nueva reseña mediante un diálogo emergente, proporcionando
// un título y contenido para la reseña. Además, si la reseña pertenece al usuario logueado,
// se muestra un botón de eliminar que permite eliminarla de la base de datos. La interfaz incluye
// un botón flotante (+) para agregar nuevas reseñas y cada reseña muestra el nombre del usuario
// y su contenido.


@Composable
fun ReviewsScreen(viewModel: ReviewsViewModel) {
    var showDialog by remember { mutableStateOf(false) }
    var userName by remember { mutableStateOf("Usuario Anónimo") }
    var currentUserUID by remember { mutableStateOf<String?>(null) }

    // Obtener el nombre del usuario logueado y su UID
    val currentUser = FirebaseAuth.getInstance().currentUser
    if (currentUser != null) {
        currentUserUID = currentUser.uid
        val firestore = FirebaseFirestore.getInstance()
        firestore.collection("users")
            .document(currentUser.uid)
            .get()
            .addOnSuccessListener { document ->
                userName = document.getString("name") ?: currentUser.displayName ?: "Usuario Anónimo"
            }
    }

    LaunchedEffect(Unit) {
        viewModel.loadReviews()
    }

    val reviews by viewModel.reviews.collectAsState()

    Scaffold(
        topBar = { AppTopBar(
            titleText = "Reseñas",
            onMenuClick = TODO()
        ) },
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Text(
                    text = "+",
                    style = MaterialTheme.typography.headlineSmall.copy(fontSize = 30.sp) // Ajusta el tamaño aquí
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End, // Asegura que el botón flote a la derecha
        content = { paddingValues ->
            // Ajustamos el padding superior para que no tape el FAB
            val bottomPadding = paddingValues.calculateBottomPadding() + 80.dp // Ajuste adicional para evitar que el FAB lo tape
            Column(modifier = Modifier.padding(paddingValues).padding(bottom = bottomPadding)) {
                LazyColumn {
                    items(reviews) { review ->
                        ReviewItem(
                            review = review,
                            currentUserUID = currentUserUID,
                            onDelete = { viewModel.deleteReview(review.id) }
                        )
                    }
                }
            }
        }
    )

    if (showDialog) {
        AddReviewDialog(
            onDismiss = { showDialog = false },
            onAddReview = { newReview ->
                viewModel.addReview(newReview)
                showDialog = false
            },
            userName = userName
        )
    }
}

@Composable
fun ReviewItem(review: ReviewState, currentUserUID: String?, onDelete: (ReviewState) -> Unit) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Muestra el icono y el nombre del usuario en la parte superior
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Icono de Usuario",
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = review.userName,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))

            // Título y contenido de la reseña
            Text(text = review.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = review.content, style = MaterialTheme.typography.bodyMedium)

            // Solo mostrar el botón de eliminar si el UID del usuario coincide
            if (review.userUID == currentUserUID) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    IconButton(onClick = { onDelete(review) }) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Eliminar",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
}





@Composable
fun AddReviewDialog(onDismiss: () -> Unit, onAddReview: (ReviewState) -> Unit, userName: String) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Agregar Reseña") },
        text = {
            Column {
                TextField(value = title, onValueChange = { title = it }, label = { Text("Título") })
                TextField(value = content, onValueChange = { content = it }, label = { Text("Contenido") })
            }
        },
        confirmButton = {
            TextButton(onClick = {
                if (title.isNotBlank() && content.isNotBlank()) {
                    onAddReview(ReviewState("", title, content, userName = userName))
                }
            }) {
                Text("Agregar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewReviewItem() {
    ReviewItem(
        review = ReviewState(
            id = "1",
            title = "Título de la Reseña",
            content = "Este es el contenido de la reseña. Es un comentario largo sobre el producto o servicio.",
            userName = "Juan Pérez",
            userUID = "12345"
        ),
        currentUserUID = "12345",
        onDelete = {}
    )
}





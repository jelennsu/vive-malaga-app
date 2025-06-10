package com.elena.practica3b.ui.screens.editprofile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.elena.practica3b.R
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.ui.draw.clip
import android.widget.Toast
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.elena.practica3b.ui.components.ConfirmActionDialog
import com.elena.practica3b.ui.theme.Practica3BTheme

@Composable
fun EditProfileScreen(
    viewModel: EditProfileViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val context = LocalContext.current
    var newImageUri by remember { mutableStateOf<Uri?>(null) }
    val state = viewModel.state

    var showConfirmDialog by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? -> newImageUri = uri }

    LaunchedEffect(true) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                is EditProfileUiEvent.ProfileUpdated -> {
                    Toast.makeText(context, "Perfil actualizado", Toast.LENGTH_SHORT).show()
                    // Limpiamos URI local tras guardar para mostrar imagen remota actualizada
                    newImageUri = null
                }
                is EditProfileUiEvent.ShowError -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    if (state.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        EditProfileContent(
            state = state,
            newImageUri = newImageUri,
            onNameChanged = { viewModel.onNameChanged(it) },
            onPhoneChanged = { viewModel.onPhoneChanged(it) },
            onSaveClick = {
                viewModel.updateUserData(state.name, state.phone, newImageUri)
            },
            onBack = { navController.popBackStack() },
            onPickImage = { launcher.launch("image/*") },
            onDeleteImage = { showConfirmDialog = true } // Abrimos diálogo en lugar de borrar directamente
        )
    }

    if (showConfirmDialog) {
        ConfirmActionDialog(
            title = "Eliminar imagen de perfil",
            message = "¿Está seguro que desea eliminar su imagen de perfil?",
            onConfirm = {
                showConfirmDialog = false
                newImageUri = null
                viewModel.deleteProfileImage()
            },
            onDismiss = { showConfirmDialog = false }
        )
    }
}


@Composable
fun EditProfileContent(
    state: EditProfileState,
    newImageUri: Uri?,
    onNameChanged: (String) -> Unit,
    onPhoneChanged: (String) -> Unit,
    onSaveClick: () -> Unit,
    onBack: () -> Unit,
    onPickImage: () -> Unit,
    onDeleteImage: () -> Unit,
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState) // <- scroll aquí
            .padding(16.dp)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Editar perfil",
                style = MaterialTheme.typography.titleLarge
            )
        }
        Spacer(Modifier.height(16.dp))

        val imageModel: Any? = when {
            newImageUri != null -> newImageUri
            state.imageUrl.isNotEmpty() -> state.imageUrl
            else -> null
        }

        AsyncImage(
            model = imageModel,
            contentDescription = "Foto de perfil",
            placeholder = painterResource(R.drawable.defecto),
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .clickable { onPickImage() }
                .align(Alignment.CenterHorizontally),
            contentScale = ContentScale.Crop
        )

        Spacer(Modifier.height(8.dp))

        // Mostrar botón eliminar solo si NO hay una nueva URI y la imagen no es la predeterminada
        if (newImageUri == null && !state.imageUrl.contains("userdefault.jpg")) {
            TextButton(
                onClick = onDeleteImage,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Eliminar imagen de perfil")
            }

            Spacer(Modifier.height(8.dp))
        }

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = state.name,
            onValueChange = onNameChanged,
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = state.phone,
            onValueChange = onPhoneChanged,
            label = { Text("Teléfono") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = onSaveClick,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Guardar cambios")
        }
    }
}


@Preview
@Composable
fun EditProfilePreview() {
    Practica3BTheme {
        EditProfileContent(
            state = EditProfileState(
                name = "Elena",
                phone = "123456789",
                imageUrl = "https://example.com/image.jpg"
            ),
            newImageUri = null,
            onNameChanged = {},
            onPhoneChanged = {},
            onSaveClick = {},
            onBack = {},
            onPickImage = {},
            onDeleteImage = {}
        )
    }
}


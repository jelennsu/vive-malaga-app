package com.elena.practica3b.ui.screens.drawer

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.outlined.DeleteSweep
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.ModeEdit
import androidx.compose.material.icons.outlined.PowerSettingsNew
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.elena.practica3b.navigation.AppScreens
import com.elena.practica3b.ui.components.ConfirmActionDialog
import com.elena.practica3b.ui.components.EventDialog
import com.elena.practica3b.ui.theme.Practica3BTheme
import com.elena.practica3b.R

@Composable
fun DrawerScreen(
    drawerViewModel: DrawerViewModel = hiltViewModel(),
    onCloseDrawer: () -> Unit,
    navController: NavHostController,
) {
    val state by drawerViewModel.state
    var showLogoutDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var feedbackMessage by remember { mutableStateOf<Int?>(null) }

    if (showLogoutDialog) {
        ConfirmActionDialog(
            title = "Confirmación",
            message = "¿Deseas cerrar sesión?",
            onConfirm = {
                showLogoutDialog = false
                drawerViewModel.logout()
            },
            onDismiss = { showLogoutDialog = false }
        )
    }

    if (showDeleteDialog) {
        ConfirmActionDialog(
            title = "Eliminar cuenta",
            message = "¿Estás seguro de que deseas eliminar tu cuenta? Todos sus datos se borrarán. Esta acción no se puede deshacer.",
            confirmText = "Eliminar",
            dismissText = "Cancelar",
            onConfirm = {
                println("ConfirmActionDialog onConfirm triggered")
                showDeleteDialog = false
                drawerViewModel.deleteAccount { success, messageResId ->
                    if (success) {
                        navController.navigate(AppScreens.LoginScreen.route) {
                            popUpTo(AppScreens.HomeScreen.route) { inclusive = true }
                        }
                    } else {
                        feedbackMessage = messageResId
                    }
                }
            },
            onDismiss = {
                showDeleteDialog = false
            }
        )
    }

    if (feedbackMessage != null) {
        EventDialog(
            message = feedbackMessage!!,
            isError = true,
            onDismiss = {
                feedbackMessage = null
            }
        )
    }



    // Navega al login cuando el usuario ya no está logueado
    LaunchedEffect(state.userName) {
        if (state.userName == "") {
            navController.navigate(AppScreens.LoginScreen.route) {
                popUpTo(AppScreens.HomeScreen.route) { inclusive = true }
            }
        }
    }

    Drawer(
        userName = state.userName,
        profileImageUrl = state.profileImageUrl,
        navController = navController,
        onLogout = {
            showLogoutDialog = true
            onCloseDrawer()
        },
        onDeleteAccount = {
            showDeleteDialog = true
            onCloseDrawer()
        },
        onCloseDrawer = onCloseDrawer
    )
}

@Composable
fun Drawer(
    userName: String,
    profileImageUrl: String?,
    navController: NavHostController,
    onLogout: () -> Unit,
    onDeleteAccount: () -> Unit,
    onCloseDrawer: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(230.dp)
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(Modifier.height(16.dp))

        // Reemplazamos el Icon fijo por ProfileImage dinámico
        ProfileImage(profileImageUrl = profileImageUrl)

        Spacer(Modifier.height(8.dp))

        Text(
            text = userName,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(start = 4.dp),
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(Modifier.height(24.dp))

        DrawerItem(
            text = "Editar perfil",
            onClick = {
                onCloseDrawer()
                navController.navigate(AppScreens.EditProfileScreen.route)
            },
            icon = Icons.Outlined.ModeEdit
        )
        DrawerItem(
            text = "Cerrar sesión",
            onClick = {
                onCloseDrawer()
                onLogout()
            },
            icon = Icons.Outlined.PowerSettingsNew
        )
        DrawerItem(
            text = "Eliminar cuenta",
            onClick = {
                onCloseDrawer()
                onDeleteAccount()
            },
            icon = Icons.Outlined.DeleteSweep
        )
        DrawerItem(
            text = "Historial",
            onClick = {
                onCloseDrawer()
                navController.navigate(AppScreens.HistorialScreen.route)
            },
            icon = Icons.Outlined.History
        )

    }
}

@Composable
fun DrawerItem(
    text: String,
    onClick: () -> Unit,
    icon: ImageVector,
    iconTint: Color = MaterialTheme.colorScheme.onSurface
) {
    TextButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(0.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 6.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                tint = iconTint
            )

            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Start,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}


@Composable
fun ProfileImage(profileImageUrl: String?) {
    if (!profileImageUrl.isNullOrEmpty()) {
        AsyncImage(
            model = profileImageUrl,
            contentDescription = "Imagen de perfil",
            modifier = Modifier
                .size(46.dp)
                .clip(CircleShape),  // <- esto recorta la imagen en círculo
            placeholder = painterResource(R.drawable.defecto),
            error = painterResource(R.drawable.defecto),
            contentScale = ContentScale.Crop
        )
    } else {
        Image(
            painter = painterResource(R.drawable.defecto),
            contentDescription = "Imagen por defecto",
            modifier = Modifier
                .size(58.dp)
                .clip(CircleShape)
                .padding(start = 8.dp),
            contentScale = ContentScale.Crop
        )
    }
}



@Preview
@Composable
fun DrawerPreview() {
    Practica3BTheme {
        Drawer(
            userName = "Jelen",
            navController = rememberNavController(),
            onLogout = {},
            onDeleteAccount = {},
            onCloseDrawer = {},
            profileImageUrl = ""
        )
    }
}
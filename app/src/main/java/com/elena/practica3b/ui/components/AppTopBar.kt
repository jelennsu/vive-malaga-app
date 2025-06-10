package com.elena.practica3b.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.elena.practica3b.R
import com.elena.practica3b.ui.screens.drawer.DrawerViewModel
import com.elena.practica3b.ui.theme.Practica3BTheme

// AppTopBar: Barra superior personalizada con un título, íconos de menú y usuario.

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    titleText: String,
    onMenuClick: () -> Unit
) {
    val drawerViewModel: DrawerViewModel = hiltViewModel()
    val state by drawerViewModel.state
    val profileImageUrl = state.profileImageUrl

    TopAppBar(
        title = { Text(text = titleText) },
        navigationIcon = {
            IconButton(onClick = onMenuClick) {
                if (!profileImageUrl.isNullOrEmpty()) {
                    AsyncImage(
                        model = profileImageUrl,
                        contentDescription = "Imagen de perfil",
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape),
                        placeholder = painterResource(R.drawable.defecto),
                        error = painterResource(R.drawable.defecto),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Abrir menú",
                        tint = MaterialTheme.colorScheme.onSecondary,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            titleContentColor = MaterialTheme.colorScheme.onSecondary
        )
    )
}



@Preview
@Composable
fun AppTopBarPreview(){
    Practica3BTheme {
        AppTopBar(titleText = "Vive Málaga", onMenuClick = {})
    }
}



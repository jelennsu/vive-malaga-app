package com.elena.practica3b.ui.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// Composable que muestra un botón redondeado con texto, o un indicador
// de progreso circular si se indica.

@Composable
fun RoundedButton(
    modifier: Modifier = Modifier,
    text: String,
    displayProgressBar: Boolean = false,
    onClick: () -> Unit
) {
    if(!displayProgressBar) {
        Button(
            modifier = modifier.width(280.dp).height(50.dp),
            onClick = onClick,
            shape = RoundedCornerShape(50),
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium.copy(
                    color = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    } else {
        CircularProgressIndicator(
            modifier = Modifier.size(50.dp),
            color = MaterialTheme.colorScheme.primary,
            strokeWidth = 6.dp
        )
    }
}
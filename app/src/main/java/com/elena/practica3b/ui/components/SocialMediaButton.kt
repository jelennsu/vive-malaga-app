package com.elena.practica3b.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

// Composable que muestra un botón con borde personalizado y color de texto según la red social.

@Composable
fun SocialMediaButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    socialMediaColor: Color
) {
    OutlinedButton(
        modifier = Modifier
            .width(280.dp)
            .height(50.dp),
        onClick = onClick,
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.Transparent,
            contentColor = socialMediaColor
        ),
        border = BorderStroke(
            width = (1.5).dp,
            color = socialMediaColor
        )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium.copy(
                color = socialMediaColor
            )
        )
    }
}
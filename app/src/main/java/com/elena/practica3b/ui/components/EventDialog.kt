package com.elena.practica3b.ui.components

import android.content.res.Configuration
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.elena.practica3b.ui.theme.Practica3BTheme

// Composable para mostrar un diálogo de evento, que puede mostrar
// un mensaje de éxito o error con un botón de "Aceptar".

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDialog(
    modifier: Modifier = Modifier,
    @StringRes message: Int,
    isError: Boolean = true,
    onDismiss: (() -> Unit)? = null
) {
    val title = if (isError) "Error" else "Éxito"

    BasicAlertDialog(onDismissRequest = { onDismiss?.invoke() },
        content = {
            Column(
                modifier = modifier
                    .background(color = MaterialTheme.colorScheme.surfaceVariant)
                    .padding(16.dp)
            ) {
                Text(
                    title,
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = LocalContext.current.getString(message),
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 16.sp
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = { onDismiss?.invoke() }) {
                        Text(text = "Aceptar", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun EventDialogPreview() {
    Practica3BTheme {

        EventDialog(
            message = android.R.string.cancel,
            onDismiss = {}
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun EventDialogDarkPreview() {
    Practica3BTheme {
        EventDialog(
            message = android.R.string.cancel,
            onDismiss = {}
        )
    }
}

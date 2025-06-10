package com.elena.practica3b.ui.screens.login

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.*
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import com.elena.practica3b.R
import com.elena.practica3b.ui.components.EventDialog
import com.elena.practica3b.ui.components.RoundedButton
import com.elena.practica3b.ui.components.TransparentTextField
import com.elena.practica3b.ui.theme.Practica3BTheme

/**
 * Pantalla de inicio de sesión.
 *
 * Esta pantalla permite al usuario iniciar sesión con su correo electrónico y contraseña.
 * Incluye:
 * - Campos de entrada para correo y contraseña.
 * - Botón para iniciar sesión.
 * - Botón flotante para ir al registro.
 * - Manejo de errores mediante un `EventDialog`.
 *
 * @param viewModel ViewModel que maneja la lógica de autenticación.
 * @param onNavigateToRegister Acción para navegar a la pantalla de registro.
 * @param onDismissDialog Acción para cerrar el cuadro de diálogo en caso de error.
 */
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onNavigateToRegister: () -> Unit,
    onDismissDialog: () -> Unit
) {
    val state = viewModel.state.value
    val emailValue = rememberSaveable { mutableStateOf("") }
    val passwordValue = rememberSaveable { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        Image(
            painter = painterResource(id = R.drawable.biznaga),
            contentDescription = "Login Image",
            contentScale = ContentScale.Inside,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
            modifier = Modifier
                .padding(top = 32.dp)
                .size(315.dp)
                .align(Alignment.TopCenter)
        )

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            ConstraintLayout {
                val (surface, fab) = createRefs()

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                        .constrainAs(surface) {
                            bottom.linkTo(parent.bottom)
                        },
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(
                        topStartPercent = 8,
                        topEndPercent = 8
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Text(
                            text = stringResource(R.string.bienvenido),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Medium
                            )
                        )

                        Text(
                            text = stringResource(R.string.inicia_sesion),
                            style = MaterialTheme.typography.headlineSmall.copy(
                                color = MaterialTheme.colorScheme.primary
                            )
                        )

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            /**
                             * Campo de entrada para el correo electrónico del usuario.
                             */
                            TransparentTextField(
                                textFieldValue = emailValue,
                                textLabel = stringResource(R.string.email),
                                keyboardType = KeyboardType.Email,
                                keyboardActions = KeyboardActions(
                                    onNext = {
                                        focusManager.moveFocus(FocusDirection.Down)
                                    }
                                ),
                                imeAction = ImeAction.Next
                            )

                            /**
                             * Campo de entrada para la contraseña del usuario con opción de visibilidad.
                             */
                            TransparentTextField(
                                textFieldValue = passwordValue,
                                textLabel = stringResource(R.string.contrasena),
                                keyboardType = KeyboardType.Password,
                                keyboardActions = KeyboardActions(
                                    onDone = {
                                        focusManager.clearFocus()
                                        viewModel.login(emailValue.value, passwordValue.value)
                                    }
                                ),
                                imeAction = ImeAction.Done,
                                trailingIcon = {
                                    IconButton(
                                        onClick = {
                                            passwordVisibility = !passwordVisibility
                                        }
                                    ) {
                                        Icon(
                                            imageVector = if (passwordVisibility) {
                                                Icons.Default.Visibility
                                            } else {
                                                Icons.Default.VisibilityOff
                                            },
                                            contentDescription = "Toggle Password Icon"
                                        )
                                    }
                                },
                                visualTransformation = if (passwordVisibility) {
                                    VisualTransformation.None
                                } else {
                                    PasswordVisualTransformation()
                                }
                            )

                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = stringResource(R.string.olvidado),
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.End,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            /**
                             * Botón para iniciar sesión con las credenciales ingresadas.
                             */
                            RoundedButton(
                                text = stringResource(R.string.iniciar),
                                displayProgressBar = state.displayProgressBar,
                                onClick = {
                                    viewModel.login(emailValue.value, passwordValue.value)
                                }
                            )

                            /**
                             * Texto con opción para navegar a la pantalla de registro.
                             */
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onNavigateToRegister()
                                    },
                                text = buildAnnotatedString {
                                    append(stringResource(R.string.todavia))

                                    withStyle(
                                        style = SpanStyle(
                                            color = MaterialTheme.colorScheme.primary,
                                            fontWeight = FontWeight.Bold
                                        )
                                    ) {
                                        append(stringResource(R.string.registrate))
                                    }
                                },
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                /**
                 * Botón flotante para navegar a la pantalla de registro.
                 */
                FloatingActionButton(
                    modifier = Modifier
                        .size(72.dp)
                        .constrainAs(fab) {
                            top.linkTo(surface.top, margin = (-36).dp)
                            end.linkTo(surface.end, margin = 36.dp)
                        },
                    containerColor = MaterialTheme.colorScheme.primary,
                    onClick = {
                        onNavigateToRegister()
                    }
                ) {
                    Icon(
                        modifier = Modifier.size(42.dp),
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Forward Icon",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }

        /**
         * Muestra un cuadro de diálogo en caso de error en la autenticación.
         */
        if (state.errorMessage != null) {
            EventDialog(message = state.errorMessage, isError = true, onDismiss = onDismissDialog)
        }
    }
}


@Preview
@Composable
fun LoginScreenPreview() {
    Practica3BTheme {
        LoginScreen(
            onNavigateToRegister = {  },
            onDismissDialog = {}
        )
    }
}


@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun LoginScreenDarkPreview() {
    Practica3BTheme {
        LoginScreen(
            onNavigateToRegister = {},
            onDismissDialog = {}
        )
    }
}


package com.elena.practica3b.ui.screens.registration

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.elena.practica3b.R
import com.elena.practica3b.ui.components.EventDialog
import com.elena.practica3b.ui.components.RoundedButton
import com.elena.practica3b.ui.components.SocialMediaButton
import com.elena.practica3b.ui.components.TransparentTextField
import com.elena.practica3b.ui.theme.FACEBOOKCOLOR
import com.elena.practica3b.ui.theme.GMAILCOLOR
import com.elena.practica3b.ui.theme.Practica3BTheme

// Pantalla de registro de usuario. Permite al usuario ingresar su nombre, correo electrónico,
// número de teléfono, contraseña y confirmación de contraseña. Tiene validación de campos y
// visibilidad toggle para las contraseñas. Además, incluye botones para registrarse y regresar
// a la pantalla anterior. Al completar el registro, muestra un cuadro de diálogo con el resultado
// y navega a la pantalla de inicio de sesión en caso de éxito.

@Composable
fun RegistrationScreen(
    navController: NavController,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val state = viewModel.state.value

    RegistrationContent(
        state = state,
        onRegister = { name, email, phone, password, confirmPassword ->
            viewModel.register(name, email, phone, password, confirmPassword)
        },
        onBack = { navController.popBackStack() },
        onDismissDialog = { viewModel.hideDialog() },
        onSuccessRegister = {
            navController.navigate("login_screen") {
                popUpTo("registration_screen") { inclusive = true }
            }
            viewModel.hideDialog()
        }
    )
}

@Composable
fun RegistrationContent(
    state: RegisterState,
    onRegister: (String, String, String, String, String) -> Unit,
    onBack: () -> Unit,
    onDismissDialog: () -> Unit,
    onSuccessRegister: () -> Unit
) {
    val nameValue = remember { mutableStateOf("") }
    val emailValue = remember { mutableStateOf("") }
    val phoneValue = remember { mutableStateOf("") }
    val passwordValue = remember { mutableStateOf("") }
    val confirmPasswordValue = remember { mutableStateOf("") }

    var passwordVisibility by remember { mutableStateOf(false) }
    var confirmPasswordVisibility by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current


    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        onBack()
                    }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back Icon",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                Text(
                    text = stringResource(R.string.crea),
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = MaterialTheme.colorScheme.primary
                    )
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TransparentTextField(
                    textFieldValue = nameValue,
                    textLabel = stringResource(R.string.nombre),
                    keyboardType = KeyboardType.Text,
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    ),
                    imeAction = ImeAction.Next
                )

                TransparentTextField(
                    textFieldValue = emailValue,
                    textLabel = stringResource(R.string.email),
                    keyboardType = KeyboardType.Email,
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    ),
                    imeAction = ImeAction.Next
                )

                TransparentTextField(
                    textFieldValue = phoneValue,
                    textLabel = stringResource(R.string.telefono),
                    keyboardType = KeyboardType.Phone,
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    ),
                    imeAction = ImeAction.Next
                )

                TransparentTextField(
                    textFieldValue = passwordValue,
                    textLabel = stringResource(R.string.contrasena),
                    keyboardType = KeyboardType.Password,
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    ),
                    imeAction = ImeAction.Next,
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                passwordVisibility = !passwordVisibility
                            }
                        ) {
                            Icon(
                                imageVector = if (passwordVisibility) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = "Toggle Password Icon"
                            )
                        }
                    },
                    visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation()
                )

                TransparentTextField(
                    textFieldValue = confirmPasswordValue,
                    textLabel = stringResource(R.string.confirmar),
                    keyboardType = KeyboardType.Password,
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()

                            onRegister(
                                nameValue.value,
                                emailValue.value,
                                phoneValue.value,
                                passwordValue.value,
                                confirmPasswordValue.value
                            )
                        }
                    ),
                    imeAction = ImeAction.Done,
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                confirmPasswordVisibility = !confirmPasswordVisibility
                            }
                        ) {
                            Icon(
                                imageVector = if (confirmPasswordVisibility) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = "Toggle Password Icon"
                            )
                        }
                    },
                    visualTransformation = if (confirmPasswordVisibility) VisualTransformation.None else PasswordVisualTransformation()
                )

                Spacer(modifier = Modifier.height(16.dp))

                RoundedButton(
                    text = "Registrarse",
                    displayProgressBar = state.displayProgressBar,
                    onClick = {
                        onRegister(
                            nameValue.value,
                            emailValue.value,
                            phoneValue.value,
                            passwordValue.value,
                            confirmPasswordValue.value
                        )
                    }
                )

                Text(
                    text = buildAnnotatedString {
                        append(stringResource(R.string.ya)) // ejemplo: "¿Ya tienes una cuenta? "

                        append(" ") // <-- espacio extra opcional

                        withStyle(
                            style = SpanStyle(
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                        ) {
                            append(stringResource(R.string.inicia)) // ejemplo: "Inicia sesión"
                        }
                    }
                    ,
                    modifier = Modifier.clickable { onBack() }
                )

            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ){
                    HorizontalDivider(
                        modifier = Modifier.width(24.dp),
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Text(
                        modifier = Modifier.padding(8.dp),
                        text = stringResource(R.string.o),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Black
                        )
                    )

                    HorizontalDivider(
                        modifier = Modifier.width(24.dp),
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.inicia_con),
                    style = MaterialTheme.typography.bodySmall.copy(
                        MaterialTheme.colorScheme.primary
                    ),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                SocialMediaButton(
                    text = stringResource(R.string.facebook),
                    onClick = {  },
                    socialMediaColor = FACEBOOKCOLOR
                )

                SocialMediaButton(
                    text = stringResource(R.string.gmail),
                    onClick = { },
                    socialMediaColor = GMAILCOLOR
                )
            }
        }

        // Reemplaza el uso de navController directamente por onSuccessRegister
        if (state.successRegister) {
            EventDialog(
                message = R.string.success_registration,
                isError = false,
                onDismiss = onSuccessRegister
            )
        }

        if (state.errorMessage != null) {
            EventDialog(message = state.errorMessage, isError = true, onDismiss = onDismissDialog)
        }

    }
}

@Preview
@Composable
fun RegistrationContentPreview() {
    Practica3BTheme {
        RegistrationContent(
            state = RegisterState(),
            onRegister = { _, _, _, _, _ -> },
            onBack = {},
            onDismissDialog = {},
            onSuccessRegister = {}
        )
    }
}

@Preview(uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun RegistrationContentDarkPreview() {
    Practica3BTheme {
        RegistrationContent(
            state = RegisterState(),
            onRegister = { _, _, _, _, _ -> },
            onBack = {},
            onDismissDialog = {},
            onSuccessRegister = {}
        )
    }
}


package com.example.helpdeskpro.vista.componente

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.helpdeskpro.R
import com.example.helpdeskpro.ui.theme.HelpdeskProTheme

private val RecuperarButtonColor = Color(0xFF8998AD)

/**
 * Formulario reutilizable para solicitar una contraseña temporal.
 * El estado, la validación y la llamada a la API se gestionan fuera del componente.
 */
@Composable
fun RecuperarContrasenaComponent(
    email: String,
    onEmailChange: (String) -> Unit,
    onEnviar: () -> Unit,
    onVolverLogin: () -> Unit,
    modifier: Modifier = Modifier,
    emailError: String? = null,
    errorMessage: String? = null,
    successMessage: String? = null,
    isLoading: Boolean = false
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = stringResource(R.string.email),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
            )
            OutlinedTextField(
                value = email,
                onValueChange = onEmailChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(stringResource(R.string.email_placeholder)) },
                singleLine = true,
                isError = emailError != null,
                supportingText = {
                    if (emailError != null) {
                        Text(emailError)
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        if (!isLoading) onEnviar()
                    }
                ),
                enabled = !isLoading
            )
        }

        if (errorMessage != null) {
            Text(
                text = errorMessage,
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        if (successMessage != null) {
            Text(
                text = successMessage,
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Button(
            onClick = onEnviar,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            enabled = !isLoading,
            colors = ButtonDefaults.buttonColors(containerColor = RecuperarButtonColor)
        ) {
            Text(
                text = stringResource(
                    if (isLoading) R.string.sending else R.string.send_temporary_password
                ),
                fontWeight = FontWeight.SemiBold
            )
        }

        TextButton(
            onClick = onVolverLogin,
            enabled = !isLoading
        ) {
            Text(stringResource(R.string.back_to_login))
        }
    }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
private fun RecuperarContrasenaComponentPreview() {
    HelpdeskProTheme(dynamicColor = false) {
        RecuperarContrasenaComponent(
            email = "usuario@empresa.com",
            onEmailChange = {},
            onEnviar = {},
            onVolverLogin = {}
        )
    }
}

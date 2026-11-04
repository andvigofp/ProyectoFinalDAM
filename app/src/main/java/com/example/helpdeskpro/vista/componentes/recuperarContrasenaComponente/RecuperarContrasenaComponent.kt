package com.example.helpdeskpro.vista.componentes.recuperarContrasenaComponente

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.helpdeskpro.R
import com.example.helpdeskpro.ui.theme.HelpdeskProTheme

private val RecuperarButtonColor = Color(0xFF8998AD)

/** Formulario para solicitar una contraseña temporal. */
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
        EmailRecuperacionComponent(
            email = email,
            onEmailChange = onEmailChange,
            onEnviar = onEnviar,
            error = emailError,
            enabled = !isLoading
        )
        MensajesRecuperacionComponent(
            errorMessage = errorMessage,
            successMessage = successMessage
        )
        Button(
            onClick = onEnviar,
            modifier = Modifier.fillMaxWidth().height(52.dp),
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
        TextButton(onClick = onVolverLogin, enabled = !isLoading) {
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

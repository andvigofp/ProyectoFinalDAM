package com.example.helpdeskpro.vista.componentes.recuperarContrasenaComponente

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.helpdeskpro.R
import com.example.helpdeskpro.ui.theme.HelpdeskProTheme

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
        BotonEnviarRecuperacionComponent(
            onClick = onEnviar,
            modifier = Modifier.fillMaxWidth(),
            isLoading = isLoading
        )
        BotonVolverLoginRecuperacionComponent(
            onClick = onVolverLogin,
            enabled = !isLoading
        )
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

package com.example.helpdeskpro.vista.pagina

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.helpdeskpro.ui.theme.HelpdeskProTheme
import com.example.helpdeskpro.vista.componente.RecuperarContrasenaComponent

private val RecuperarLogoBorderColor = Color(0xFF65758B)

/**
 * Pantalla de recuperación de contraseña sin estado propio.
 */
@Composable
fun RecuperarContrasenaPage(
    email: String = "",
    onEmailChange: (String) -> Unit = {},
    onEnviar: () -> Unit = {},
    onVolverLogin: () -> Unit = {},
    modifier: Modifier = Modifier,
    emailError: String? = null,
    errorMessage: String? = null,
    successMessage: String? = null,
    isLoading: Boolean = false
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Surface(
            modifier = Modifier.size(104.dp),
            shape = CircleShape,
            color = Color.Transparent,
            border = BorderStroke(2.dp, RecuperarLogoBorderColor)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = "LOGO",
                    color = RecuperarLogoBorderColor,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Recuperar contraseña",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Introduce tu email y te enviaremos una contraseña temporal " +
                "para recuperar el acceso a tu cuenta.",
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(24.dp))

        RecuperarContrasenaComponent(
            email = email,
            onEmailChange = onEmailChange,
            onEnviar = onEnviar,
            onVolverLogin = onVolverLogin,
            modifier = Modifier.fillMaxWidth(),
            emailError = emailError,
            errorMessage = errorMessage,
            successMessage = successMessage,
            isLoading = isLoading
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
private fun RecuperarContrasenaPagePreview() {
    HelpdeskProTheme(dynamicColor = false) {
        RecuperarContrasenaPage()
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
private fun RecuperarContrasenaExitoPreview() {
    HelpdeskProTheme(dynamicColor = false) {
        RecuperarContrasenaPage(
            email = "usuario@empresa.com",
            successMessage = "Te hemos enviado una contraseña temporal."
        )
    }
}

package com.example.helpdeskpro.vista.pagina

import android.util.Patterns
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.helpdeskpro.modelo.LoginDTO
import com.example.helpdeskpro.ui.theme.HelpdeskProTheme
import com.example.helpdeskpro.vista.componente.LoginComponent

private val LogoBorderColor = Color(0xFF65758B)

/**
 * Primera pantalla de la aplicación. Las acciones externas se conectarán a navegación/backend.
 */
@Composable
fun LoginPage(
    modifier: Modifier = Modifier,
    onLogin: (LoginDTO) -> Unit = {},
    onForgotPassword: () -> Unit = {},
    onCreateAccount: () -> Unit = {}
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var emailError by rememberSaveable { mutableStateOf<String?>(null) }
    var passwordError by rememberSaveable { mutableStateOf<String?>(null) }

    fun validateAndLogin() {
        val normalizedEmail = email.trim()
        emailError = when {
            normalizedEmail.isEmpty() -> "Introduce tu email"
            !Patterns.EMAIL_ADDRESS.matcher(normalizedEmail).matches() ->
                "Introduce un email válido"
            else -> null
        }
        passwordError = if (password.isBlank()) "Introduce tu contraseña" else null

        if (emailError == null && passwordError == null) {
            onLogin(LoginDTO(email = normalizedEmail, password = password))
        }
    }

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
            border = BorderStroke(2.dp, LogoBorderColor)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = "LOGO",
                    color = LogoBorderColor,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "HelpDesk Pro",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(32.dp))

        LoginComponent(
            email = email,
            password = password,
            onEmailChange = {
                email = it
                emailError = null
            },
            onPasswordChange = {
                password = it
                passwordError = null
            },
            onLogin = ::validateAndLogin,
            onForgotPassword = onForgotPassword,
            onCreateAccount = onCreateAccount,
            modifier = Modifier.fillMaxWidth(),
            emailError = emailError,
            passwordError = passwordError
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
private fun LoginPagePreview() {
    HelpdeskProTheme (dynamicColor = false) {
        LoginPage()
    }
}

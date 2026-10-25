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
import com.example.helpdeskpro.viemodel.ustate.LoginUiState
import com.example.helpdeskpro.vista.componente.LoginComponent

private val LogoBorderColor = Color(0xFF65758B)

/**
 * Pantalla de login sin estado propio: recibe LoginUiState y delega los eventos.
 */
@Composable
fun LoginPage(
    state: LoginUiState = LoginUiState(),
    onEmailChange: (String) -> Unit = {},
    onPasswordChange: (String) -> Unit = {},
    onLogin: () -> Unit = {},
    onForgotPassword: () -> Unit = {},
    onCreateAccount: () -> Unit = {},
    modifier: Modifier = Modifier
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
            email = state.email,
            password = state.password,
            onEmailChange = onEmailChange,
            onPasswordChange = onPasswordChange,
            onLogin = onLogin,
            onForgotPassword = onForgotPassword,
            onCreateAccount = onCreateAccount,
            modifier = Modifier.fillMaxWidth(),
            emailError = state.emailError,
            passwordError = state.passwordError,
            isLoading = state.isLoading
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

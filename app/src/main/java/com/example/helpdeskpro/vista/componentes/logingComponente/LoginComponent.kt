package com.example.helpdeskpro.vista.componentes.logingComponente

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.helpdeskpro.ui.theme.HelpdeskProTheme

/**
 * Formulario reutilizable de acceso. El estado y la validación pertenecen al ViewModel.
 */
@Composable
fun LoginComponent(
    email: String,
    password: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLogin: () -> Unit,
    onForgotPassword: () -> Unit,
    onCreateAccount: () -> Unit,
    modifier: Modifier = Modifier,
    emailError: String? = null,
    passwordError: String? = null,
    isLoading: Boolean = false
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        LoginEmailComponent(
            email = email,
            onEmailChange = onEmailChange,
            error = emailError,
            enabled = !isLoading
        )
        LoginPasswordComponent(
            password = password,
            onPasswordChange = onPasswordChange,
            error = passwordError,
            enabled = !isLoading
        )
        LoginActionsComponent(
            onLogin = onLogin,
            onForgotPassword = onForgotPassword,
            onCreateAccount = onCreateAccount,
            isLoading = isLoading
        )
    }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
private fun LoginComponentPreview() {
    HelpdeskProTheme(dynamicColor = false) {
        LoginComponent(
            email = "usuario@empresa.com",
            password = "123456",
            onEmailChange = {},
            onPasswordChange = {},
            onLogin = {},
            onForgotPassword = {},
            onCreateAccount = {}
        )
    }
}

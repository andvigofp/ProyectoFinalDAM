package com.example.helpdeskpro.vista.paginas

import androidx.compose.foundation.Image
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.helpdeskpro.R
import com.example.helpdeskpro.modelo.SesionUsuarioDTO
import com.example.helpdeskpro.ui.theme.HelpdeskProTheme
import com.example.helpdeskpro.viemodel.ustate.LoginUiState
import com.example.helpdeskpro.viemodel.vm.LoginViewModel
import com.example.helpdeskpro.vista.componentes.alertDialogComponente.ConfirmationDialog
import com.example.helpdeskpro.vista.componentes.logingComponente.BotonLoginComponent
import com.example.helpdeskpro.vista.componentes.logingComponente.CamposLoginComponent
import com.example.helpdeskpro.vista.componentes.logingComponente.LoginActionsComponent

/**
 * Pantalla de login sin estado propio: recibe LoginUiState y delega los eventos.
 *
 * @param loginViewModel ViewModel que mantiene y valida las credenciales.
 * @param modifier modificador aplicado a la pantalla.
 * @param onLoginSuccess acción ejecutada con el identificador tras iniciar sesión.
 * @param onForgotPassword acción para abrir la recuperación de contraseña.
 * @param onCreateAccount acción para abrir el registro de cuenta.
 */
@Composable
fun LoginPage(
    loginViewModel: LoginViewModel,
    modifier: Modifier = Modifier,
    onLoginSuccess: (String, String) -> Unit = { _, _ -> },
    onForgotPassword: () -> Unit = {},
    onCreateAccount: () -> Unit = {}
) {
    val uiState by loginViewModel.uiState.collectAsState()

    // Detectar inicio de sesión correcto
    LaunchedEffect(uiState.loginSuccess, uiState.userId) {
        if (uiState.loginSuccess) {
            uiState.userId?.let { userId ->
                onLoginSuccess(userId, uiState.userRole.orEmpty())
            }
        }
    }

    LoginPageContent(
        uiState = uiState,
        onEmailChange = loginViewModel::onEmailChange,
        onPasswordChange = loginViewModel::onPasswordChange,
        onLogin = { loginViewModel.iniciarSesion() },
        onForgotPassword = onForgotPassword,
        onCreateAccount = onCreateAccount,
        modifier = modifier
    )
}

@Composable
private fun LoginPageContent(
    uiState: LoginUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLogin: () -> Unit,
    onForgotPassword: () -> Unit,
    onCreateAccount: () -> Unit,
    modifier: Modifier = Modifier
) {
    var mostrarConfirmacionLogin by rememberSaveable { mutableStateOf(false) }
    val solicitarLogin = { mostrarConfirmacionLogin = true }

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
            color = Color.Transparent
        ) {
            Image(
                painter = painterResource(R.drawable.ic_logo),
                contentDescription = stringResource(R.string.logo),
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(32.dp))

        CamposLoginComponent(
            uiState = uiState,
            onEmailChange = onEmailChange,
            onPasswordChange = onPasswordChange,
            onLogin = solicitarLogin,
            modifier = Modifier.fillMaxWidth(),
        )
        uiState.errorMessage?.let { error ->
            Text(
                text = error,
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        Spacer(modifier = Modifier.height(32.dp))
        BotonLoginComponent(
            onClick = solicitarLogin,
            isLoading = uiState.isLoading
        )
        LoginActionsComponent(
            onForgotPassword = onForgotPassword,
            onCreateAccount = onCreateAccount,
            modifier = Modifier.fillMaxWidth(),
            isLoading = uiState.isLoading
        )
    }

    ConfirmationDialog(
        showDialog = mostrarConfirmacionLogin,
        onDismissRequest = { mostrarConfirmacionLogin = false },
        title = stringResource(R.string.confirm_login_title),
        message = stringResource(R.string.confirm_login_message),
        confirmButtonText = stringResource(R.string.login),
        dismissButtonText = stringResource(R.string.dialog_cancel),
        onConfirmClick = onLogin
    )
}


@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
private fun LoginPagePreview() {
    HelpdeskProTheme(dynamicColor = false) {
        LoginPageContent(
            uiState = LoginUiState(),
            onEmailChange = {},
            onPasswordChange = {},
            onLogin = {},
            onForgotPassword = {},
            onCreateAccount = {}
        )
    }
}

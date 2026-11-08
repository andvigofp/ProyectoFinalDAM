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
import com.example.helpdeskpro.ui.theme.HelpdeskProTheme
import com.example.helpdeskpro.viemodel.ustate.RecuperarContrasenaUiState
import com.example.helpdeskpro.viemodel.vm.RecuperarContrasenaViewModel
import com.example.helpdeskpro.vista.componentes.alertDialogComponente.ConfirmationDialog
import com.example.helpdeskpro.vista.componentes.recuperarContrasenaComponente.BotonEnviarRecuperacionComponent
import com.example.helpdeskpro.vista.componentes.recuperarContrasenaComponente.BotonVolverLoginRecuperacionComponent
import com.example.helpdeskpro.vista.componentes.recuperarContrasenaComponente.EmailRecuperacionComponent
import com.example.helpdeskpro.vista.componentes.recuperarContrasenaComponente.MensajesRecuperacionComponent

/**
 * Pantalla de recuperación de contraseña sin estado propio.
 *
 * @param recuperarContrasenaViewModel ViewModel que mantiene y valida el formulario.
 * @param modifier modificador aplicado a la pantalla.
 * @param onVolverLogin acción para regresar al inicio de sesión.
 */
@Composable
fun RecuperarContrasenaPage(
    recuperarContrasenaViewModel: RecuperarContrasenaViewModel,
    modifier: Modifier = Modifier,
    onVolverLogin: () -> Unit = {}
) {
    val uiState by recuperarContrasenaViewModel.uiState.collectAsState()
    var mostrarConfirmacion by rememberSaveable { mutableStateOf(false) }

    RecuperarContrasenaPageContent(
        uiState = uiState,
        onEmailChange = recuperarContrasenaViewModel::onEmailChange,
        onEnviar = { mostrarConfirmacion = true },
        onVolverLogin = onVolverLogin,
        modifier = modifier
    )

    ConfirmationDialog(
        showDialog = mostrarConfirmacion,
        onDismissRequest = { mostrarConfirmacion = false },
        title = stringResource(R.string.confirm_action_title),
        message = stringResource(R.string.confirm_recovery_message),
        confirmButtonText = stringResource(R.string.send_temporary_password),
        dismissButtonText = stringResource(R.string.dialog_cancel),
        onConfirmClick = { recuperarContrasenaViewModel.recuperarContrasena() }
    )
}

@Composable
private fun RecuperarContrasenaPageContent(
    uiState: RecuperarContrasenaUiState,
    onEmailChange: (String) -> Unit,
    onEnviar: () -> Unit,
    onVolverLogin: () -> Unit,
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
            text = stringResource(R.string.recover_password),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(R.string.recover_password_description),
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(24.dp))

        EmailRecuperacionComponent(
            email = uiState.email,
            onEmailChange = onEmailChange,
            onEnviar = onEnviar,
            modifier = Modifier.fillMaxWidth(),
            error = uiState.emailError,
            enabled = !uiState.isLoading
        )
        MensajesRecuperacionComponent(
            errorMessage = uiState.errorMessage,
            successMessage = uiState.successMessage
        )
        Spacer(modifier = Modifier.height(32.dp))
        BotonEnviarRecuperacionComponent(
            onClick = onEnviar,
            modifier = Modifier.fillMaxWidth(),
            isLoading = uiState.isLoading
        )
        Spacer(modifier = Modifier.height(12.dp))
        BotonVolverLoginRecuperacionComponent(
            onClick = onVolverLogin,
            enabled = !uiState.isLoading
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
private fun RecuperarContrasenaPagePreview() {
    HelpdeskProTheme(dynamicColor = false) {
        RecuperarContrasenaPageContent(
            uiState = RecuperarContrasenaUiState(),
            onEmailChange = {},
            onEnviar = {},
            onVolverLogin = {}
        )
    }
}
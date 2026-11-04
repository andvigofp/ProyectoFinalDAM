package com.example.helpdeskpro.vista.paginas

import android.app.Application
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.helpdeskpro.R
import com.example.helpdeskpro.ui.theme.HelpdeskProTheme
import com.example.helpdeskpro.viemodel.vm.RecuperarContrasenaViewModel
import com.example.helpdeskpro.vista.componentes.recuperarContrasenaComponente.RecuperarContrasenaComponent

private val RecuperarLogoBorderColor = Color(0xFF65758B)

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
                    text = stringResource(R.string.logo),
                    color = RecuperarLogoBorderColor,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium
                )
            }
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

        RecuperarContrasenaComponent(
            email = uiState.email,
            onEmailChange = recuperarContrasenaViewModel::onEmailChange,
            onEnviar = recuperarContrasenaViewModel::recuperarContrasena,
            onVolverLogin = onVolverLogin,
            modifier = Modifier.fillMaxWidth(),
            emailError = uiState.emailError,
            errorMessage = uiState.errorMessage,
            successMessage = uiState.successMessage,
            isLoading = uiState.isLoading
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
private fun RecuperarContrasenaPagePreview() {
    HelpdeskProTheme(dynamicColor = false) {
        val recuperarContrasenaViewModel = remember {
            RecuperarContrasenaViewModel(Application())
        }

        RecuperarContrasenaPage(
            recuperarContrasenaViewModel = recuperarContrasenaViewModel,
            modifier = Modifier,
            onVolverLogin = {}
        )
    }
}
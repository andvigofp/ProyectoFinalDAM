package com.example.helpdeskpro.vista.pagina

import android.app.Application
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.helpdeskpro.R
import com.example.helpdeskpro.ui.theme.HelpdeskProTheme
import com.example.helpdeskpro.viemodel.vm.CrearCuentaViewModel
import com.example.helpdeskpro.vista.componente.crearCuentaComponente.CrearCuentaComponent

/**
 * Pantalla de registro sin estado propio: recibe los datos y delega los eventos.
 *
 * @param crearCuentaViewModel ViewModel que mantiene y valida el formulario.
 * @param modifier modificador aplicado a la pantalla.
 * @param onVolverLogin acción para regresar al inicio de sesión.
 */
@Composable
fun CrearCuentaPage(
    crearCuentaViewModel: CrearCuentaViewModel,
    modifier: Modifier = Modifier,
    onVolverLogin: () -> Unit = {},
) {

    val uiState by crearCuentaViewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onVolverLogin,
                enabled = !uiState.isLoading
            ) {
                Text(
                    text = "‹",
                    style = MaterialTheme.typography.headlineMedium
                )
            }
            Text(
                text = stringResource(R.string.create_account),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        CrearCuentaComponent(
            nombre = uiState.nombre,
            apellidos = uiState.apellidos,
            email = uiState.email,
            telefono = uiState.telefono,
            password = uiState.password,
            rol = uiState.rol,
            departamento = uiState.departamento,

            onNombreChange = crearCuentaViewModel::onNombreChange,
            onApellidosChange = crearCuentaViewModel::onApellidosChange,
            onEmailChange = crearCuentaViewModel::onEmailChange,
            onTelefonoChange = crearCuentaViewModel::onTelefonoChange,
            onPasswordChange = crearCuentaViewModel::onPasswordChange,
            onRolChange = crearCuentaViewModel::onRolChange,
            onDepartamentoChange = crearCuentaViewModel::onDepartamentoChange,

            onGuardar = {
                crearCuentaViewModel.crearCuenta()
            },

            modifier = Modifier.fillMaxWidth(),

            nombreError = uiState.nombreError,
            apellidosError = uiState.apellidosError,
            emailError = uiState.emailError,
            telefonoError = uiState.telefonoError,
            passwordError = uiState.passwordError,
            rolError = uiState.rolError,
            departamentoError = uiState.departamentoError,
            errorMessage = uiState.errorMessage,
            isLoading = uiState.isLoading
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun CrearCuentaPagePreview() {
    HelpdeskProTheme(dynamicColor = false) {
        val crearCuentaViewModel = remember { CrearCuentaViewModel(Application()) }

        CrearCuentaPage(
            crearCuentaViewModel = crearCuentaViewModel,
            modifier = Modifier,
            onVolverLogin = {}
        )
    }
}
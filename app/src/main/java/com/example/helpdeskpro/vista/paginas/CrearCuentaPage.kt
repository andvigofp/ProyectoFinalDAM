package com.example.helpdeskpro.vista.paginas

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.background
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.helpdeskpro.R
import com.example.helpdeskpro.ui.theme.HelpdeskProTheme
import com.example.helpdeskpro.viemodel.ustate.CrearCuentaUiState
import com.example.helpdeskpro.viemodel.vm.CrearCuentaViewModel
import com.example.helpdeskpro.vista.componentes.crearCuentaComponente.BotonGuardarCuentaComponent
import com.example.helpdeskpro.vista.componentes.crearCuentaComponente.BotonVolverCrearCuentaComponent
import com.example.helpdeskpro.vista.componentes.crearCuentaComponente.CamposCrearCuentaComponent

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

    CrearCuentaPageContent(
        uiState = uiState,
        onNombreChange = crearCuentaViewModel::onNombreChange,
        onApellidosChange = crearCuentaViewModel::onApellidosChange,
        onEmailChange = crearCuentaViewModel::onEmailChange,
        onTelefonoChange = crearCuentaViewModel::onTelefonoChange,
        onPasswordChange = crearCuentaViewModel::onPasswordChange,
        onRolChange = crearCuentaViewModel::onRolChange,
        onDepartamentoChange = crearCuentaViewModel::onDepartamentoChange,
        onGuardar = { crearCuentaViewModel.crearCuenta() },
        onVolverLogin = onVolverLogin,
        modifier = modifier
    )
}

@Composable
private fun CrearCuentaPageContent(
    uiState: CrearCuentaUiState,
    onNombreChange: (String) -> Unit,
    onApellidosChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onTelefonoChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onRolChange: (String) -> Unit,
    onDepartamentoChange: (String) -> Unit,
    onGuardar: () -> Unit,
    onVolverLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 22.dp, vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BotonVolverCrearCuentaComponent(
                onClick = onVolverLogin,
                enabled = !uiState.isLoading
            )
            Text(
                text = stringResource(R.string.create_account),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        CamposCrearCuentaComponent(
            uiState = uiState,
            onNombreChange = onNombreChange,
            onApellidosChange = onApellidosChange,
            onEmailChange = onEmailChange,
            onTelefonoChange = onTelefonoChange,
            onPasswordChange = onPasswordChange,
            onRolChange = onRolChange,
            onDepartamentoChange = onDepartamentoChange,
            onGuardar = onGuardar,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(72.dp))
        BotonGuardarCuentaComponent(
            onClick = onGuardar,
            isLoading = uiState.isLoading
        )
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun CrearCuentaPagePreview() {
    HelpdeskProTheme(dynamicColor = false) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.surface
        ) {
            CrearCuentaPageContent(
                uiState = CrearCuentaUiState(),
                onNombreChange = {},
                onApellidosChange = {},
                onEmailChange = {},
                onTelefonoChange = {},
                onPasswordChange = {},
                onRolChange = {},
                onDepartamentoChange = {},
                onGuardar = {},
                onVolverLogin = {}
            )
        }
    }
}
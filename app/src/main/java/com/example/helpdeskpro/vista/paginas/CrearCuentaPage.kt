package com.example.helpdeskpro.vista.paginas

import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.helpdeskpro.R
import com.example.helpdeskpro.ui.theme.HelpdeskProTheme
import com.example.helpdeskpro.viemodel.ustate.CrearCuentaUiState
import com.example.helpdeskpro.viemodel.vm.CrearCuentaViewModel
import com.example.helpdeskpro.vista.componentes.alertDialogComponente.ConfirmationDialog
import com.example.helpdeskpro.vista.componentes.alertDialogComponente.SuccessDialog
import com.example.helpdeskpro.vista.componentes.crearCuentaComponente.BotonGuardarCuentaComponent
import com.example.helpdeskpro.vista.componentes.crearCuentaComponente.CamposCrearCuentaComponent
import com.example.helpdeskpro.vista.componentes.toolbarComponenete.ToolbarComponent

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
    onCuentaCreada: () -> Unit = {},
) {

    val uiState by crearCuentaViewModel.uiState.collectAsState()
    var mostrarConfirmacion by rememberSaveable { mutableStateOf(false) }
    var mostrarExito by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(uiState.cuentaCreada) {
        if (uiState.cuentaCreada) mostrarExito = true
    }

    CrearCuentaPageContent(
        uiState = uiState,
        onNombreChange = crearCuentaViewModel::onNombreChange,
        onApellidosChange = crearCuentaViewModel::onApellidosChange,
        onEmailChange = crearCuentaViewModel::onEmailChange,
        onTelefonoChange = crearCuentaViewModel::onTelefonoChange,
        onPasswordChange = crearCuentaViewModel::onPasswordChange,
        onDepartamentoChange = crearCuentaViewModel::onDepartamentoChange,
        onGuardar = { mostrarConfirmacion = true },
        onVolverLogin = onVolverLogin,
        modifier = modifier
    )

    ConfirmationDialog(
        showDialog = mostrarConfirmacion,
        onDismissRequest = { mostrarConfirmacion = false },
        title = stringResource(R.string.confirm_action_title),
        message = stringResource(R.string.confirm_create_account_message),
        confirmButtonText = stringResource(R.string.create_account),
        dismissButtonText = stringResource(R.string.dialog_cancel),
        onConfirmClick = { crearCuentaViewModel.crearCuenta() }
    )

    SuccessDialog(
        showDialog = mostrarExito,
        onDismissRequest = {
            mostrarExito = false
            onCuentaCreada()
        },
        message = stringResource(R.string.account_created_success)
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
        ToolbarComponent(
            titulo = stringResource(R.string.create_account),
            onVolver = onVolverLogin,
            backEnabled = !uiState.isLoading
        )

        Spacer(modifier = Modifier.height(20.dp))

        CamposCrearCuentaComponent(
            uiState = uiState,
            onNombreChange = onNombreChange,
            onApellidosChange = onApellidosChange,
            onEmailChange = onEmailChange,
            onTelefonoChange = onTelefonoChange,
            onPasswordChange = onPasswordChange,
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
                onDepartamentoChange = {},
                onGuardar = {},
                onVolverLogin = {}
            )
        }
    }
}
package com.example.helpdeskpro.vista.paginas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.helpdeskpro.R
import com.example.helpdeskpro.modelo.EditarPerfilDTO
import com.example.helpdeskpro.modelo.PerfilDTO
import com.example.helpdeskpro.ui.theme.HelpdeskProTheme
import com.example.helpdeskpro.viemodel.ustate.EditarPerfilUiState
import com.example.helpdeskpro.viemodel.vm.EditarPerfilViewModel
import com.example.helpdeskpro.vista.componentes.alertDialogComponente.ConfirmationDialog
import com.example.helpdeskpro.vista.componentes.editarPerfilComponente.BotonGuardarEditarPerfilComponent
import com.example.helpdeskpro.vista.componentes.editarPerfilComponente.CamposEditarPerfilComponent
import com.example.helpdeskpro.vista.componentes.toolbarComponenete.ToolbarComponent

@Composable
fun EditarPerfilPage(
    editarPerfilViewModel: EditarPerfilViewModel,
    perfil: PerfilDTO,
    modifier: Modifier = Modifier,
    onVolver: () -> Unit = {},
    onGuardar: (EditarPerfilDTO) -> Unit = {}
) {
    val uiState by editarPerfilViewModel.uiState.collectAsState()
    var mostrarConfirmacion by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(perfil.id) {
        editarPerfilViewModel.inicializar(perfil)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 22.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        ToolbarComponent(
            titulo = stringResource(R.string.edit_profile),
            onVolver = onVolver
        )
        Spacer(modifier = Modifier.height(12.dp))

        if (uiState.perfil == null) {
            Text(
                text = stringResource(R.string.no_profile_data),
                modifier = Modifier.padding(top = 24.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            CamposEditarPerfilComponent(
                uiState = uiState,
                onNombreChange = editarPerfilViewModel::onNombreChange,
                onApellidosChange = editarPerfilViewModel::onApellidosChange,
                onEmailChange = editarPerfilViewModel::onEmailChange,
                onTelefonoChange = editarPerfilViewModel::onTelefonoChange,
                onContrasenaChange = editarPerfilViewModel::onContrasenaChange
            )

            uiState.errorMessage?.let { error ->
                Text(
                    text = error,
                    modifier = Modifier.padding(top = 8.dp),
                    color = MaterialTheme.colorScheme.error
                )
            }

            Spacer(modifier = Modifier.height(28.dp))
            BotonGuardarEditarPerfilComponent(
                onClick = { mostrarConfirmacion = true },
                enabled = !uiState.isLoading
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    ConfirmationDialog(
        showDialog = mostrarConfirmacion,
        onDismissRequest = { mostrarConfirmacion = false },
        title = stringResource(R.string.confirm_action_title),
        message = stringResource(R.string.confirm_save_profile_message),
        confirmButtonText = stringResource(R.string.save),
        dismissButtonText = stringResource(R.string.dialog_cancel),
        onConfirmClick = { editarPerfilViewModel.guardarPerfil(onGuardar) }
    )
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun EditarPerfilPagePreview() {
    HelpdeskProTheme(dynamicColor = false) {
        Surface(color = MaterialTheme.colorScheme.surface) {
            EditarPerfilPageContentPreview()
        }
    }
}

@Composable
private fun EditarPerfilPageContentPreview() {
    val perfil = PerfilDTO(
        id = 1L,
        nombre = "Ana",
        apellidos = "Pérez García",
        email = "ana@empresa.com",
        telefono = "600 123 456",
        departamento = "Ventas",
        miembroDesde = "01/09/2026",
        rol = "EMPLEADO"
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 22.dp, vertical = 16.dp)
    ) {
        ToolbarComponent(
            titulo = stringResource(R.string.edit_profile),
            onVolver = {}
        )
        Spacer(modifier = Modifier.height(12.dp))
        CamposEditarPerfilComponent(
            uiState = EditarPerfilUiState(perfil = perfil),
            onNombreChange = {},
            onApellidosChange = {},
            onEmailChange = {},
            onTelefonoChange = {},
            onContrasenaChange = {}
        )
        Spacer(modifier = Modifier.height(28.dp))
        BotonGuardarEditarPerfilComponent(onClick = {}, enabled = true)
    }
}

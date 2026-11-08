package com.example.helpdeskpro.vista.paginas

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.helpdeskpro.R
import com.example.helpdeskpro.ui.theme.HelpdeskProTheme
import com.example.helpdeskpro.viemodel.vm.NuevaIncidenciaViewModel
import com.example.helpdeskpro.vista.componentes.alertDialogComponente.ConfirmationDialog
import com.example.helpdeskpro.vista.componentes.nuevaIncidenciaComponente.NuevaIncidenciaComponent
import com.example.helpdeskpro.vista.componentes.toolbarComponenete.ToolbarComponent

/**
 * Pantalla para que el empleado registre una nueva incidencia.
 *
 * @param nuevaIncidenciaViewModel ViewModel que mantiene y valida el formulario.
 * @param modifier modificador aplicado a la página.
 * @param onVolver acción para regresar a la pantalla anterior.
 * @param onIncidenciaCreada acción ejecutada tras crear la incidencia.
 */
@Composable
fun NuevaIncidenciaPage(
    nuevaIncidenciaViewModel: NuevaIncidenciaViewModel,
    modifier: Modifier = Modifier,
    onVolver: () -> Unit = {},
    onIncidenciaCreada: () -> Unit = {}
) {
    val uiState by nuevaIncidenciaViewModel.uiState.collectAsState()
    var mostrarConfirmacion by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        ToolbarComponent(
            titulo = stringResource(R.string.new_incident),
            onVolver = onVolver,
            backEnabled = !uiState.isLoading
        )

        Spacer(modifier = Modifier.height(20.dp))

        NuevaIncidenciaComponent(
            titulo = uiState.titulo,
            descripcion = uiState.descripcion,
            categoria = uiState.categoria,
            prioridad = uiState.prioridad,
            onTituloChange = nuevaIncidenciaViewModel::onTituloChange,
            onDescripcionChange = nuevaIncidenciaViewModel::onDescripcionChange,
            onCategoriaChange = nuevaIncidenciaViewModel::onCategoriaChange,
            onPrioridadChange = nuevaIncidenciaViewModel::onPrioridadChange,
            onCrear = { mostrarConfirmacion = true },
            categorias = listOf(
                stringResource(R.string.hardware),
                stringResource(R.string.software),
                stringResource(R.string.network),
                stringResource(R.string.email_category)
            ),
            prioridades = listOf(
                stringResource(R.string.low),
                stringResource(R.string.medium),
                stringResource(R.string.high),
                stringResource(R.string.urgent)
            ),
            modifier = Modifier.fillMaxWidth(),
            tituloError = uiState.tituloError,
            descripcionError = uiState.descripcionError,
            categoriaError = uiState.categoriaError,
            prioridadError = uiState.prioridadError,
            errorMessage = uiState.errorMessage,
            isLoading = uiState.isLoading
        )
        
        Spacer(modifier = Modifier.height(24.dp))
    }

    ConfirmationDialog(
        showDialog = mostrarConfirmacion,
        onDismissRequest = { mostrarConfirmacion = false },
        title = stringResource(R.string.confirm_action_title),
        message = stringResource(R.string.confirm_create_incident_message),
        confirmButtonText = stringResource(R.string.create_incident),
        dismissButtonText = stringResource(R.string.dialog_cancel),
        onConfirmClick = { nuevaIncidenciaViewModel.crearIncidencia() }
    )

    androidx.compose.runtime.LaunchedEffect(uiState.incidenciaCreada) {
        if (uiState.incidenciaCreada) onIncidenciaCreada()
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun NuevaIncidenciaPagePreview() {
    HelpdeskProTheme(dynamicColor = false) {
        val nuevaIncidenciaViewModel = remember {
            NuevaIncidenciaViewModel(Application(), userId = "preview")
        }

        NuevaIncidenciaPage(
            nuevaIncidenciaViewModel = nuevaIncidenciaViewModel,
            modifier = Modifier,
            onVolver = {},
            onIncidenciaCreada = {}
        )
    }
}

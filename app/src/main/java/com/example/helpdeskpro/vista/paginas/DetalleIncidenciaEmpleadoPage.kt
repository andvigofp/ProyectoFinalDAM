package com.example.helpdeskpro.vista.paginas

import android.app.Application
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.helpdeskpro.R
import com.example.helpdeskpro.ui.theme.HelpdeskProTheme
import com.example.helpdeskpro.viemodel.vm.DetalleIncidenciaEmpleadoViewModel
import com.example.helpdeskpro.vista.componentes.alertDialogComponente.ConfirmationDialog
import com.example.helpdeskpro.vista.componentes.alertDialogComponente.SuccessDialog
import com.example.helpdeskpro.vista.componentes.detalleIncidenciaEmpleadoComponente.CabeceraDetalleIncidenciaEmpleadoComponent
import com.example.helpdeskpro.vista.componentes.detalleIncidenciaEmpleadoComponente.ComentariosIncidenciaEmpleadoComponent
import com.example.helpdeskpro.vista.componentes.detalleIncidenciaEmpleadoComponente.HistorialIncidenciaEmpleadoComponent
import com.example.helpdeskpro.vista.componentes.homeEmpleadoComponete.HomeBottomBarComponent
import com.example.helpdeskpro.vista.componentes.homeEmpleadoComponete.HomeTab
import com.example.helpdeskpro.vista.componentes.toolbarComponenete.ToolbarDetalleIncidenciaComponent

/**
 * Pantalla de detalle de una incidencia para el empleado que la creó.
 * Muestra la cabecera, los comentarios y el historial, y permite añadir comentarios.
 *
 * @param viewModel ViewModel con el detalle y el estado de la pantalla.
 * @param modifier modificador aplicado a la pantalla.
 * @param onVolver acción para regresar a la pantalla anterior.
 * @param onInicio acción de navegación a Inicio.
 * @param onNueva acción de navegación al formulario de nueva incidencia.
 * @param onPerfil acción de navegación al perfil.
 */
@Composable
fun DetalleIncidenciaEmpleadoPage(
    viewModel: DetalleIncidenciaEmpleadoViewModel,
    modifier: Modifier = Modifier,
    onVolver: () -> Unit = {},
    onInicio: () -> Unit = {},
    onNueva: () -> Unit = {},
    onPerfil: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val detalle = uiState.detalle
    var mostrarConfirmacion by rememberSaveable { mutableStateOf(false) }
    var mostrarExito by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(viewModel.incidenciaId) {
        viewModel.cargarDetalle()
    }

    LaunchedEffect(uiState.comentarioEnviado) {
        if (uiState.comentarioEnviado) mostrarExito = true
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.surface,
        bottomBar = {
            HomeBottomBarComponent(
                selectedTab = HomeTab.INICIO,
                onInicio = onInicio,
                onNueva = onNueva,
                onPerfil = onPerfil
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 22.dp, vertical = 16.dp)
        ) {
            ToolbarDetalleIncidenciaComponent(onVolver = onVolver)
            Spacer(modifier = Modifier.height(22.dp))

            when {
                uiState.isLoading && detalle == null -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                detalle == null -> {
                    Text(
                        text = uiState.errorMessage
                            ?: stringResource(R.string.no_incident_detail),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 48.dp),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                else -> {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(22.dp)
                    ) {
                        CabeceraDetalleIncidenciaEmpleadoComponent(incidencia = detalle)

                        if (uiState.errorMessage != null) {
                            Text(
                                text = uiState.errorMessage.orEmpty(),
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }

                        ComentariosIncidenciaEmpleadoComponent(
                            comentarios = detalle.comentarios,
                            comentario = uiState.comentario,
                            comentarioError = uiState.comentarioError,
                            isSending = uiState.isSending,
                            onComentarioChange = viewModel::onComentarioChange,
                            onEnviarComentario = { mostrarConfirmacion = true }
                        )
                        HistorialIncidenciaEmpleadoComponent(historial = detalle.historial)
                    }
                }
            }
        }
    }

    ConfirmationDialog(
        showDialog = mostrarConfirmacion,
        onDismissRequest = { mostrarConfirmacion = false },
        title = stringResource(R.string.confirm_action_title),
        message = stringResource(R.string.confirm_send_comment_message),
        confirmButtonText = stringResource(R.string.employee_send_comment),
        dismissButtonText = stringResource(R.string.dialog_cancel),
        onConfirmClick = { viewModel.enviarComentario() }
    )

    SuccessDialog(
        showDialog = mostrarExito,
        onDismissRequest = { mostrarExito = false },
        message = stringResource(R.string.comment_sent_success)
    )
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun DetalleIncidenciaEmpleadoPagePreview() {
    HelpdeskProTheme(dynamicColor = false) {
        val viewModel = remember {
            DetalleIncidenciaEmpleadoViewModel(
                Application(),
                userId = "preview",
                incidenciaId = 1L
            )
        }

        DetalleIncidenciaEmpleadoPage(viewModel = viewModel)
    }
}
package com.example.helpdeskpro.vista.paginas

import android.app.Application
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
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
import com.example.helpdeskpro.modelo.DetalleIncidenciaTecnicoDTO
import com.example.helpdeskpro.ui.theme.HelpdeskProTheme
import com.example.helpdeskpro.viemodel.vm.DetalleEmpleadoViewModel
import com.example.helpdeskpro.vista.componentes.alertDialogComponente.ConfirmationDialog
import com.example.helpdeskpro.vista.componentes.detalleIncidenciaTecnicoAdminComponente.BotonGuardarEstadoComponent
import com.example.helpdeskpro.vista.componentes.detalleIncidenciaTecnicoAdminComponente.BotonRegistrarSolucionComponent
import com.example.helpdeskpro.vista.componentes.detalleIncidenciaTecnicoAdminComponente.CabeceraDetalleIncidenciaComponent
import com.example.helpdeskpro.vista.componentes.detalleIncidenciaTecnicoAdminComponente.ComentariosHistorialComponent
import com.example.helpdeskpro.vista.componentes.detalleIncidenciaTecnicoAdminComponente.NavegacionInferiorDetalleComponent
import com.example.helpdeskpro.vista.componentes.detalleIncidenciaTecnicoAdminComponente.SelectorEstadoComponent
import com.example.helpdeskpro.vista.componentes.detalleIncidenciaTecnicoAdminComponente.SolucionIncidenciaComponent
import com.example.helpdeskpro.vista.componentes.toolbarComponenete.ToolbarDetalleIncidenciaComponent

/**
 * Pantalla de detalle que conecta el estado del ViewModel con los componentes visuales.
 *
 * @param detalleEmpleadoViewModel ViewModel con el detalle y el estado de la pantalla.
 * @param cargarDetalle función suspendida que obtiene los datos de la incidencia.
 * @param modifier modificador aplicado a la pantalla.
 * @param onVolver acción para regresar a la pantalla anterior.
 * @param onGuardarEstado acción ejecutada con el nuevo estado seleccionado.
 * @param onRegistrarSolucion acción ejecutada con el texto de la solución.
 * @param onIncidencias acción de navegación al listado de incidencias.
 * @param onPerfil acción de navegación al perfil.
 */
@Composable
fun DetalleIncideciaTecnicoPage(
    detalleEmpleadoViewModel: DetalleEmpleadoViewModel,
    cargarDetalle: suspend () -> DetalleIncidenciaTecnicoDTO,
    modifier: Modifier = Modifier,
    onVolver: () -> Unit = {},
    onGuardarEstado: (String) -> Unit = {},
    onRegistrarSolucion: (String) -> Unit = {},
    onIncidencias: () -> Unit = {},
    onPerfil: () -> Unit = {}
) {
    val uiState by detalleEmpleadoViewModel.uiState.collectAsState()
    val detalle = uiState.detalle
    var confirmarGuardarEstado by rememberSaveable { mutableStateOf(false) }
    var confirmarRegistrarSolucion by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(detalleEmpleadoViewModel.incidenciaId) {
        detalleEmpleadoViewModel.cargarDetalle(cargarDetalle)
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.surface,
        bottomBar = {
            NavegacionInferiorDetalleComponent(
                onIncidencias = onIncidencias,
                onPerfil = onPerfil
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 22.dp, vertical = 16.dp),
        ) {
            ToolbarDetalleIncidenciaComponent(onVolver = onVolver)
            Spacer(modifier = Modifier.height(24.dp))

            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                uiState.errorMessage != null -> {
                    Text(
                        text = uiState.errorMessage.orEmpty(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 48.dp),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                detalle == null -> {
                    Text(
                        text = stringResource(R.string.no_incident_detail),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 48.dp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                else -> Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(22.dp)
                ) {
                    CabeceraDetalleIncidenciaComponent(
                        incidencia = detalle
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                        SelectorEstadoComponent(
                            estado = uiState.estadoSeleccionado,
                            estados = listOf(
                                stringResource(R.string.open),
                                stringResource(R.string.in_process),
                                stringResource(R.string.resolved)
                            ),
                            onEstadoChange = detalleEmpleadoViewModel::onEstadoChange
                        )
                        BotonGuardarEstadoComponent(
                            onGuardar = { confirmarGuardarEstado = true },
                            enabled = uiState.estadoSeleccionado.isNotBlank()
                        )
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                        SolucionIncidenciaComponent(
                            solucion = uiState.solucion,
                            onSolucionChange = detalleEmpleadoViewModel::onSolucionChange
                        )
                        BotonRegistrarSolucionComponent(
                            onRegistrar = { confirmarRegistrarSolucion = true },
                            enabled = uiState.solucion.isNotBlank()
                        )
                    }

                    ComentariosHistorialComponent()
                }
            }

            ConfirmationDialog(
                showDialog = confirmarGuardarEstado,
                onDismissRequest = { confirmarGuardarEstado = false },
                title = stringResource(R.string.confirm_action_title),
                message = stringResource(R.string.confirm_save_incident_status_message),
                confirmButtonText = stringResource(R.string.save_status),
                dismissButtonText = stringResource(R.string.dialog_cancel),
                onConfirmClick = {
                    detalleEmpleadoViewModel.guardarEstado(onGuardarEstado)
                }
            )

            ConfirmationDialog(
                showDialog = confirmarRegistrarSolucion,
                onDismissRequest = { confirmarRegistrarSolucion = false },
                title = stringResource(R.string.confirm_action_title),
                message = stringResource(R.string.confirm_register_solution_message),
                confirmButtonText = stringResource(R.string.register_solution),
                dismissButtonText = stringResource(R.string.dialog_cancel),
                onConfirmClick = {
                    detalleEmpleadoViewModel.registrarSolucion(onRegistrarSolucion)
                }
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun DetalleIncidenciaTecnicoPreview() {
    HelpdeskProTheme(dynamicColor = false) {
        val viewModel = remember {
            DetalleEmpleadoViewModel(
                Application(),
                userId = "preview",
                incidenciaId = 1L
            )
        }

        DetalleIncideciaTecnicoPage(
            detalleEmpleadoViewModel = viewModel,
            cargarDetalle = {
                DetalleIncidenciaTecnicoDTO(
                    id = 1L,
                    titulo = "No enciende el ordenador",
                    descripcion = "El ordenador no arranca al pulsar el botón de encendido.",
                    estado = "ABIERTA",
                    creador = "Ana Pérez",
                    departamento = "Ventas",
                    categoria = "Hardware",
                    prioridad = "Alta",
                    fechaCreacion = "15/10/2026"
                )
            }
        )
    }
}

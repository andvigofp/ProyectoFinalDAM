package com.example.helpdeskpro.vista.paginas

import android.app.Application
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.helpdeskpro.R
import com.example.helpdeskpro.modelo.DetalleIncidenciaTecnicoDTO
import com.example.helpdeskpro.ui.theme.HelpdeskProTheme
import com.example.helpdeskpro.viemodel.vm.DetalleEmpleadoViewModel
import com.example.helpdeskpro.vista.componentes.detalleIncidenciaTecnicoComponente.DetalleIncidenciaTecnicoComponent
import com.example.helpdeskpro.vista.componentes.detalleIncidenciaTecnicoComponente.NavegacionInferiorDetalleComponent

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

    LaunchedEffect(detalleEmpleadoViewModel.incidenciaId) {
        detalleEmpleadoViewModel.cargarDetalle(cargarDetalle)
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Color.White,
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
            DetalleIncidenciaTecnicoComponent(
                incidencia = uiState.detalle,
                estadoSeleccionado = uiState.estadoSeleccionado,
                estados = listOf(
                    stringResource(R.string.open),
                    stringResource(R.string.in_process),
                    stringResource(R.string.resolved)
                ),
                solucion = uiState.solucion,
                onVolver = onVolver,
                onEstadoChange = detalleEmpleadoViewModel::onEstadoChange,
                onGuardarEstado = {
                    detalleEmpleadoViewModel.guardarEstado(onGuardarEstado)
                },
                onSolucionChange = detalleEmpleadoViewModel::onSolucionChange,
                onRegistrarSolucion = {
                    detalleEmpleadoViewModel.registrarSolucion(onRegistrarSolucion)
                },
                isLoading = uiState.isLoading,
                errorMessage = uiState.errorMessage
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

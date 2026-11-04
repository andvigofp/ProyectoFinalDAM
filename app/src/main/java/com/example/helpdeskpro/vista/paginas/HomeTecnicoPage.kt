package com.example.helpdeskpro.vista.paginas

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.helpdeskpro.R
import com.example.helpdeskpro.modelo.HomeTecnicoDTO
import com.example.helpdeskpro.ui.theme.HelpdeskProTheme
import com.example.helpdeskpro.viemodel.ustate.HomeTecnicoUiState
import com.example.helpdeskpro.viemodel.vm.HomeTecnicoViewModel
import com.example.helpdeskpro.vista.componentes.detalleIncidenciaTecnicoComponente.NavegacionInferiorDetalleComponent
import com.example.helpdeskpro.vista.componentes.homeTecnicoComponente.HomeTecnicoComponent

/**
 * Página principal del técnico con el listado de incidencias asignadas.
 *
 * @param homeTecnicoViewModel ViewModel con las incidencias y filtros de la página.
 * @param cargarIncidencias función suspendida que obtiene las incidencias asignadas.
 * @param modifier modificador aplicado a la página.
 * @param onIncidenciaClick acción ejecutada al seleccionar una incidencia.
 * @param onPerfil acción de navegación al perfil.
 */
@Composable
fun HomeTecnicoPage(
    homeTecnicoViewModel: HomeTecnicoViewModel,
    cargarIncidencias: suspend () -> List<HomeTecnicoDTO>,
    modifier: Modifier = Modifier,
    onIncidenciaClick: (HomeTecnicoDTO) -> Unit = {},
    onPerfil: () -> Unit = {}
) {
    val uiState by homeTecnicoViewModel.uiState.collectAsState()

    LaunchedEffect(homeTecnicoViewModel.userId) {
        homeTecnicoViewModel.cargarIncidencias(cargarIncidencias)
    }

    HomeTecnicoPageContent(
        uiState = uiState,
        onFiltroEstadoChange = homeTecnicoViewModel::onFiltroEstadoChange,
        onIncidenciaClick = onIncidenciaClick,
        onPerfil = onPerfil,
        titulo = stringResource(R.string.assigned_incidents),
        etiquetasFiltro = listOf(
            stringResource(R.string.filter_open),
            stringResource(R.string.filter_in_progress),
            stringResource(R.string.filter_resolved)
        ),
        textoCreador = { creador ->
            stringResource(R.string.incident_created_by, creador)
        },
        modifier = modifier
    )
}

@Composable
private fun HomeTecnicoPageContent(
    uiState: HomeTecnicoUiState,
    onFiltroEstadoChange: (String?) -> Unit,
    onIncidenciaClick: (HomeTecnicoDTO) -> Unit,
    onPerfil: () -> Unit,
    titulo: String,
    etiquetasFiltro: List<String>,
    textoCreador: @Composable (String) -> String,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Color.White,
        bottomBar = {
            NavegacionInferiorDetalleComponent(
                onIncidencias = {},
                onPerfil = onPerfil
            )
        }
    ) { innerPadding ->
        HomeTecnicoComponent(
            incidencias = uiState.incidencias,
            filtroEstado = uiState.filtroEstado,
            incidenciasFiltradas = uiState.incidenciasFiltradas,
            onFiltroEstadoChange = onFiltroEstadoChange,
            onIncidenciaClick = onIncidenciaClick,
            titulo = titulo,
            etiquetasFiltro = etiquetasFiltro,
            textoCreador = textoCreador,
            modifier = Modifier.padding(innerPadding).fillMaxSize(),
            isLoading = uiState.isLoading,
            errorMessage = uiState.errorMessage
        )
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun HomeTecnicoPagePreview() {
    HelpdeskProTheme(dynamicColor = false) {
        val incidencias = listOf(
            HomeTecnicoDTO(
                id = 1,
                titulo = "No enciende el ordenador",
                categoria = "Hardware",
                prioridad = "Alta",
                fecha = "15/10/2026",
                estado = "ABIERTA",
                creador = "Ana Pérez"
            ),
            HomeTecnicoDTO(
                id = 2,
                titulo = "VPN no conecta en casa",
                categoria = "Red",
                prioridad = "Media",
                fecha = "16/10/2026",
                estado = "EN PROGRESO",
                creador = "Carlos Ruiz"
            )
        )
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.White
        ) {
            HomeTecnicoPageContent(
                uiState = HomeTecnicoUiState(incidencias = incidencias),
                onFiltroEstadoChange = {},
                onIncidenciaClick = {},
                titulo = "Incidencias asignadas",
                etiquetasFiltro = listOf("Abiertas", "En progreso", "Resueltas"),
                textoCreador = { creador -> "De: $creador" },
                onPerfil = {}
            )
        }
    }
}

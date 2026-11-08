package com.example.helpdeskpro.vista.paginas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.helpdeskpro.R
import com.example.helpdeskpro.modelo.HomeTecnicoDTO
import com.example.helpdeskpro.ui.theme.HelpdeskProTheme
import com.example.helpdeskpro.viemodel.ustate.HomeTecnicoUiState
import com.example.helpdeskpro.viemodel.vm.HomeTecnicoViewModel
import com.example.helpdeskpro.vista.componentes.homeAdminComponente.AdminBottomBarComponent
import com.example.helpdeskpro.vista.componentes.homeAdminComponente.AdminTab
import com.example.helpdeskpro.vista.componentes.detalleIncidenciaTecnicoAdminComponente.NavegacionInferiorDetalleComponent
import com.example.helpdeskpro.vista.componentes.homeTecnicoComponente.FiltrosEstadoTecnicoComponent
import com.example.helpdeskpro.vista.componentes.homeTecnicoComponente.IncidenciaAsignadaCardComponent
import com.example.helpdeskpro.vista.componentes.toolbarComponenete.ToolbarComponent

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
    onPerfil: () -> Unit = {},
    modoAdmin: Boolean = false,
    onPanel: () -> Unit = {},
    onUsuarios: () -> Unit = {},
    tituloPersonalizado: String? = null
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
        modoAdmin = modoAdmin,
        onPanel = onPanel,
        onUsuarios = onUsuarios,
        titulo = tituloPersonalizado ?: stringResource(R.string.assigned_incidents),
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
    modifier: Modifier = Modifier,
    modoAdmin: Boolean = false,
    onPanel: () -> Unit = {},
    onUsuarios: () -> Unit = {}
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.surface,
        bottomBar = {
            if (modoAdmin) {
                AdminBottomBarComponent(
                    selectedTab = AdminTab.INCIDENCIAS,
                    onPanel = onPanel,
                    onIncidencias = {},
                    onUsuarios = onUsuarios,
                    onPerfil = onPerfil
                )
            } else {
                NavegacionInferiorDetalleComponent(
                    onIncidencias = {},
                    onPerfil = onPerfil
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentPadding = PaddingValues(
                start = 16.dp,
                top = 20.dp,
                end = 16.dp,
                bottom = 24.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                ToolbarComponent(
                    titulo = titulo,
                    modifier = Modifier.padding(start = 6.dp),
                    titleStyle = MaterialTheme.typography.titleLarge.copy(
                        fontSize = 20.sp,
                        lineHeight = 24.sp,
                        letterSpacing = 0.sp
                    )
                )
                Spacer(modifier = Modifier.height(54.dp))
            }

            if (!uiState.isLoading && uiState.errorMessage == null &&
                uiState.incidencias.isNotEmpty()
            ) {
                item {
                    FiltrosEstadoTecnicoComponent(
                        filtroSeleccionado = uiState.filtroEstado,
                        onFiltroSeleccionado = onFiltroEstadoChange,
                        etiquetas = etiquetasFiltro
                    )
                }
            }

            when {
                uiState.isLoading -> item {
                    EstadoListaHomeTecnico {
                        CircularProgressIndicator()
                    }
                }

                uiState.errorMessage != null -> item {
                    EstadoListaHomeTecnico {
                        Text(
                            text = uiState.errorMessage,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }

                uiState.incidenciasFiltradas.isEmpty() -> item {
                    EstadoListaHomeTecnico {
                        Text(
                            text = stringResource(
                                if (uiState.incidencias.isEmpty()) {
                                    R.string.no_assigned_incidents
                                } else {
                                    R.string.no_incidents_for_filter
                                }
                            ),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }

                else -> items(
                    items = uiState.incidenciasFiltradas,
                    key = { incidencia -> incidencia.id }
                ) { incidencia ->
                    IncidenciaAsignadaCardComponent(
                        incidencia = incidencia,
                        onClick = { onIncidenciaClick(incidencia) },
                        textoCreador = textoCreador
                    )
                }
            }
        }
    }
}

@Composable
private fun EstadoListaHomeTecnico(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 48.dp),
        contentAlignment = Alignment.Center
    ) {
        content()
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
            color = MaterialTheme.colorScheme.surface
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

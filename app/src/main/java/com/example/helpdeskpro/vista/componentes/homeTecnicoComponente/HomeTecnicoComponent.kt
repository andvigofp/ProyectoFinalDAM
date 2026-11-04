package com.example.helpdeskpro.vista.componentes.homeTecnicoComponente

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import com.example.helpdeskpro.R
import com.example.helpdeskpro.modelo.HomeTecnicoDTO
import com.example.helpdeskpro.ui.theme.HelpdeskProTheme

@Composable
fun HomeTecnicoComponent(
    incidencias: List<HomeTecnicoDTO>,
    filtroEstado: String?,
    incidenciasFiltradas: List<HomeTecnicoDTO>,
    onFiltroEstadoChange: (String?) -> Unit,
    onIncidenciaClick: (HomeTecnicoDTO) -> Unit,
    titulo: String,
    etiquetasFiltro: List<String>,
    textoCreador: @Composable (String) -> String,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    errorMessage: String? = null
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = 16.dp,
            top = 20.dp,
            end = 16.dp,
            bottom = 24.dp
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column {
                Text(
                    text = titulo,
                    modifier = Modifier.padding(start = 6.dp),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontSize = 20.sp,
                        lineHeight = 24.sp,
                        letterSpacing = 0.sp
                    ),
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF414A5A)
                )
                Spacer(modifier = Modifier.height(54.dp))
            }
        }

        if (!isLoading && errorMessage == null && incidencias.isNotEmpty()) {
            item {
                FiltrosEstadoTecnicoComponent(
                    filtroSeleccionado = filtroEstado,
                    onFiltroSeleccionado = onFiltroEstadoChange,
                    etiquetas = etiquetasFiltro
                )
            }
        }

        when {
            isLoading -> item {
                EstadoListaHomeTecnico {
                    CircularProgressIndicator()
                }
            }

            errorMessage != null -> item {
                EstadoListaHomeTecnico {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            incidenciasFiltradas.isEmpty() -> item {
                EstadoListaHomeTecnico {
                    Text(
                        text = stringResource(
                            if (incidencias.isEmpty()) {
                                R.string.no_assigned_incidents
                            } else {
                                R.string.no_incidents_for_filter
                            }
                        ),
                        color = Color(0xFF8796AD),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            else -> items(
                items = incidenciasFiltradas,
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
private fun HomeTecnicoComponentPreview() {
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
            HomeTecnicoComponent(
                incidencias = incidencias,
                filtroEstado = null,
                incidenciasFiltradas = incidencias,
                onFiltroEstadoChange = {},
                onIncidenciaClick = {},
                titulo = "Incidencias asignadas",
                etiquetasFiltro = listOf("Abiertas", "En progreso", "Resueltas"),
                textoCreador = { creador -> "De: $creador" }
            )
        }
    }
}

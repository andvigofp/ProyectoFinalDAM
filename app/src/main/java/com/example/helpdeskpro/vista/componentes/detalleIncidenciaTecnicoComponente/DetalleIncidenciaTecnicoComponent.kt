package com.example.helpdeskpro.vista.componentes.detalleIncidenciaTecnicoComponente

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.helpdeskpro.R
import com.example.helpdeskpro.modelo.DetalleIncidenciaTecnicoDTO
import com.example.helpdeskpro.ui.theme.HelpdeskProTheme

/**
 * Compone el contenido completo del detalle de una incidencia para el técnico.
 */
@Composable
fun DetalleIncidenciaTecnicoComponent(
    incidencia: DetalleIncidenciaTecnicoDTO?,
    estadoSeleccionado: String,
    estados: List<String>,
    solucion: String,
    onVolver: () -> Unit,
    onEstadoChange: (String) -> Unit,
    onGuardarEstado: () -> Unit,
    onSolucionChange: (String) -> Unit,
    onRegistrarSolucion: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    errorMessage: String? = null
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(22.dp)
    ) {
        when {
            isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            errorMessage != null -> {
                Text(
                    text = errorMessage,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 48.dp),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            incidencia == null -> {
                Text(
                    text = stringResource(R.string.no_incident_detail),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 48.dp),
                    color = Color(0xFF8A8F98),
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            else -> {
                CabeceraDetalleIncidenciaComponent(
                    incidencia = incidencia,
                    onVolver = onVolver
                )

                Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                    SelectorEstadoComponent(
                        estado = estadoSeleccionado,
                        estados = estados,
                        onEstadoChange = onEstadoChange
                    )
                    BotonGuardarEstadoComponent(
                        onGuardar = onGuardarEstado,
                        enabled = estadoSeleccionado.isNotBlank()
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                    SolucionIncidenciaComponent(
                        solucion = solucion,
                        onSolucionChange = onSolucionChange
                    )
                    BotonRegistrarSolucionComponent(
                        onRegistrar = onRegistrarSolucion,
                        enabled = solucion.isNotBlank()
                    )
                }

                ComentariosHistorialComponent()
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun DetalleIncidenciaTecnicoComponentPreview() {
    HelpdeskProTheme(dynamicColor = false) {
        DetalleIncidenciaTecnicoComponent(
            incidencia = DetalleIncidenciaTecnicoDTO(
                id = 1L,
                titulo = "No enciende el ordenador",
                descripcion = "El ordenador no arranca al pulsar el botón de encendido.",
                estado = "ABIERTA",
                creador = "Ana Pérez",
                departamento = "Ventas",
                categoria = "Hardware",
                prioridad = "Alta",
                fechaCreacion = "15/10/2026"
            ),
            estadoSeleccionado = "",
            estados = listOf(
                stringResource(R.string.open),
                stringResource(R.string.in_process),
                stringResource(R.string.resolved)
            ),
            solucion = "",
            onVolver = {},
            onEstadoChange = {},
            onGuardarEstado = {},
            onSolucionChange = {},
            onRegistrarSolucion = {}
        )
    }
}

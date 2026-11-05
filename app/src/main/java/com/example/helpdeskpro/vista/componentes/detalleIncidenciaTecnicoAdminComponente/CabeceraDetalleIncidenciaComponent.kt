package com.example.helpdeskpro.vista.componentes.detalleIncidenciaTecnicoAdminComponente

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.helpdeskpro.R
import com.example.helpdeskpro.modelo.DetalleIncidenciaTecnicoDTO
import com.example.helpdeskpro.ui.theme.HelpdeskProTheme

/**
 * Muestra el título y los datos principales de una incidencia.
 *
 * @param incidencia detalle de la incidencia que se muestra.
 * @param onVolver callback para regresar a la pantalla anterior.
 * @param modifier modificador aplicado a la cabecera.
 */
@Composable
fun CabeceraDetalleIncidenciaComponent(
    incidencia: DetalleIncidenciaTecnicoDTO,
    onVolver: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            IconButton(
                onClick = onVolver,
                modifier = Modifier.size(28.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.volver),
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
            }

            Text(
                text = stringResource(R.string.incident_detail),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontSize = 20.sp,
                    lineHeight = 24.sp
                ),
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text(
                text = incidencia.titulo,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 18.sp,
                    lineHeight = 24.sp
                ),
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Column(verticalArrangement = Arrangement.spacedBy(0.dp)) {
                Text(
                    text = stringResource(
                        R.string.incident_detail_created_by,
                        incidencia.creador,
                        incidencia.departamento
                    ),
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = stringResource(
                        R.string.incident_detail_category_priority,
                        incidencia.categoria,
                        incidencia.prioridad
                    ),
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = stringResource(
                        R.string.incident_detail_created,
                        incidencia.fechaCreacion
                    ),
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

    }
}

@Preview(showBackground = true, widthDp = 390)
@Composable
private fun CabeceraDetalleIncidenciaComponentPreview() {
    HelpdeskProTheme(dynamicColor = false) {
        CabeceraDetalleIncidenciaComponent(
            incidencia = DetalleIncidenciaTecnicoDTO(
                id = 1L,
                titulo = stringResource(R.string.pc_no_enciende),
                descripcion = stringResource(R.string.pc_no_enciende_description),
                estado = stringResource(R.string.resolved),
                creador = "Ana Pérez",
                departamento = "Ventas",
                categoria = "Hardware",
                prioridad = "Alta",
                fechaCreacion = "15/10/2026"
            ),
            onVolver = {}
        )
    }
}

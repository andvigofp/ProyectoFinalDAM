package com.example.helpdeskpro.vista.componentes.homeTecnicoComponente

import androidx.compose.ui.res.colorResource

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import com.example.helpdeskpro.R
import com.example.helpdeskpro.modelo.HomeTecnicoDTO
import com.example.helpdeskpro.ui.theme.HelpdeskProTheme

@Composable
fun IncidenciaAsignadaCardComponent(
    incidencia: HomeTecnicoDTO,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    textoCreador: @Composable (String) -> String
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = incidencia.titulo,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        letterSpacing = 0.sp
                    ),
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.width(8.dp))
                EstadoIncidenciaTecnicoBadge(estado = incidencia.estado)
            }
            Text(
                text = stringResource(
                    R.string.incident_meta,
                    incidencia.categoria,
                    incidencia.prioridad,
                    incidencia.fecha
                ),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                    letterSpacing = 0.sp
                )
            )
            Text(
                text = textoCreador(incidencia.creador),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                    letterSpacing = 0.sp
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun EstadoIncidenciaTecnicoBadge(estado: String) {
    val normalizedState = estado.trim().uppercase().replace('_', ' ')
    val backgroundColor = when (normalizedState) {
        "ABIERTA" -> colorResource(R.color.status_warning)
        "EN PROGRESO" -> MaterialTheme.colorScheme.primary
        "RESUELTA" -> MaterialTheme.colorScheme.tertiary
        else -> MaterialTheme.colorScheme.primary
    }

    Surface(
        shape = RoundedCornerShape(6.dp),
        color = backgroundColor,
        contentColor = when (normalizedState) {
            "ABIERTA" -> MaterialTheme.colorScheme.onSurface
            "RESUELTA" -> MaterialTheme.colorScheme.onTertiary
            else -> MaterialTheme.colorScheme.onPrimary
        }
    ) {
        Text(
            text = estado.replace('_', ' ').uppercase(),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 11.sp,
                lineHeight = 14.sp,
                letterSpacing = 0.sp
            ),
            maxLines = 1
        )
    }
}

@Preview(showBackground = true, widthDp = 390)
@Composable
private fun IncidenciaAsignadaCardComponentPreview() {
    HelpdeskProTheme(dynamicColor = false) {
        IncidenciaAsignadaCardComponent(
            incidencia = HomeTecnicoDTO(
                id = 1,
                titulo = "No enciende el ordenador",
                categoria = "Hardware",
                prioridad = "Alta",
                fecha = "15/10/2026",
                estado = "ABIERTA",
                creador = "Ana Pérez"
            ),
            onClick = {},
            modifier = Modifier.fillMaxWidth(),
            textoCreador = { creador -> "De: $creador" }
        )
    }
}

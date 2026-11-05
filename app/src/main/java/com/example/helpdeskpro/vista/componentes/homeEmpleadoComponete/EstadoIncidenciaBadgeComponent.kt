package com.example.helpdeskpro.vista.componentes.homeEmpleadoComponete

import com.example.helpdeskpro.R

import androidx.compose.ui.res.colorResource

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun EstadoIncidenciaBadgeComponent(estado: String) {
    val normalizedState = estado.uppercase().replace(" ", "_")
    val backgroundColor = when (normalizedState) {
        "ABIERTA" -> colorResource(R.color.status_warning)
        "EN_PROGRESO" -> MaterialTheme.colorScheme.secondary
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
            style = MaterialTheme.typography.labelSmall
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EstadoIncidenciaBadgeComponentPreview() {
    EstadoIncidenciaBadgeComponent(estado = "EN_PROGRESO")
}

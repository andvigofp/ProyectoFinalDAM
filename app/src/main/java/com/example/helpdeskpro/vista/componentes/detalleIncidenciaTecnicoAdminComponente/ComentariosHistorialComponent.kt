package com.example.helpdeskpro.vista.componentes.detalleIncidenciaTecnicoAdminComponente

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.helpdeskpro.R
import com.example.helpdeskpro.ui.theme.HelpdeskProTheme


/**
 * Muestra la etiqueta de acceso a los comentarios y al historial de la incidencia.
 *
 * @param modifier modificador aplicado al texto.
 */
@Composable
fun ComentariosHistorialComponent(
    modifier: Modifier = Modifier
) {
    Text(
        text = stringResource(R.string.incident_comments_history),
        modifier = modifier.fillMaxWidth(),
        style = MaterialTheme.typography.bodySmall.copy(
            fontSize = 12.sp,
            lineHeight = 16.sp
        ),
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Preview(showBackground = true, widthDp = 390)
@Composable
private fun ComentariosHistorialComponentPreview() {
    HelpdeskProTheme(dynamicColor = false) {
        ComentariosHistorialComponent()
    }
}

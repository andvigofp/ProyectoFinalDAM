package com.example.helpdeskpro.vista.componentes.detalleIncidenciaEmpleadoComponente

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.helpdeskpro.R
import com.example.helpdeskpro.modelo.ButtonData
import com.example.helpdeskpro.modelo.ComentarioIncidenciaDTO
import com.example.helpdeskpro.modelo.DetalleIncidenciaEmpleadoDTO
import com.example.helpdeskpro.modelo.HistorialIncidenciaDTO
import com.example.helpdeskpro.ui.theme.HelpdeskProTheme
import com.example.helpdeskpro.vista.componentes.buttonsComponente.AppButton
import com.example.helpdeskpro.vista.componentes.homeEmpleadoComponete.EstadoIncidenciaBadgeComponent

/**
 * Muestra el número, el estado, el título y la descripción de la incidencia.
 *
 * @param incidencia detalle de la incidencia que se muestra.
 * @param modifier modificador aplicado a la cabecera.
 */
@Composable
fun CabeceraDetalleIncidenciaEmpleadoComponent(
    incidencia: DetalleIncidenciaEmpleadoDTO,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.employee_incident_number, incidencia.id),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            EstadoIncidenciaBadgeComponent(estado = incidencia.estado)
        }
        Spacer(modifier = Modifier.height(18.dp))
        Text(
            text = incidencia.titulo,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(14.dp))
        Text(
            text = stringResource(R.string.description),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = incidencia.descripcion,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

/**
 * Muestra los comentarios de la incidencia y el campo para escribir uno nuevo.
 *
 * @param comentarios comentarios existentes de la incidencia.
 * @param comentario texto del comentario en edición.
 * @param comentarioError error de validación del comentario, si existe.
 * @param isSending indica si el comentario se está enviando.
 * @param onComentarioChange callback cuando cambia el texto del comentario.
 * @param onEnviarComentario callback al pulsar el botón Enviar.
 * @param modifier modificador aplicado al bloque.
 */
@Composable
fun ComentariosIncidenciaEmpleadoComponent(
    comentarios: List<ComentarioIncidenciaDTO>,
    comentario: String,
    comentarioError: String?,
    isSending: Boolean,
    onComentarioChange: (String) -> Unit,
    onEnviarComentario: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
        Spacer(modifier = Modifier.height(18.dp))
        Text(
            text = stringResource(R.string.employee_comments),
            style = MaterialTheme.typography.titleSmall
        )
        Spacer(modifier = Modifier.height(14.dp))

        if (comentarios.isEmpty()) {
            Text(
                text = stringResource(R.string.employee_no_comments),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(12.dp))
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                comentarios.forEach { item ->
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        color = MaterialTheme.colorScheme.surface,
                        border = BorderStroke(
                            1.dp,
                            MaterialTheme.colorScheme.outlineVariant
                        )
                    ) {
                        Text(
                            text = stringResource(
                                R.string.employee_comment_by,
                                item.autor,
                                item.texto
                            ),
                            modifier = Modifier.padding(12.dp),
                            style = MaterialTheme.typography.bodySmall,
                            lineHeight = MaterialTheme.typography.bodyMedium.lineHeight
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
        }

        OutlinedTextField(
            value = comentario,
            onValueChange = onComentarioChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(stringResource(R.string.employee_write_comment)) },
            singleLine = true,
            isError = comentarioError != null,
            supportingText = comentarioError?.let { message -> { Text(message) } },
            shape = RoundedCornerShape(10.dp)
        )
        AppButton(
            data = ButtonData(
                nombre = if (isSending) R.string.sending else R.string.employee_send_comment,
                enabled = comentario.isNotBlank() && !isSending
            ),
            onClick = onEnviarComentario,
            modifier = Modifier
                .align(Alignment.End)
                .padding(top = 12.dp)
                .height(52.dp)
        )
    }
}

/**
 * Muestra el historial de cambios de la incidencia.
 *
 * @param historial entradas del historial de la incidencia.
 * @param modifier modificador aplicado al bloque.
 */
@Composable
fun HistorialIncidenciaEmpleadoComponent(
    historial: List<HistorialIncidenciaDTO>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.employee_history),
            style = MaterialTheme.typography.titleSmall
        )
        Spacer(modifier = Modifier.height(12.dp))
        if (historial.isEmpty()) {
            Text(
                text = stringResource(R.string.employee_no_history),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                historial.forEach { item ->
                    Text(
                        text = stringResource(
                            R.string.employee_history_entry,
                            item.fecha,
                            item.descripcion
                        ),
                        modifier = Modifier.padding(horizontal = 10.dp),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 390)
@Composable
private fun CabeceraDetalleIncidenciaEmpleadoComponentPreview() {
    HelpdeskProTheme(dynamicColor = false) {
        CabeceraDetalleIncidenciaEmpleadoComponent(
            incidencia = DetalleIncidenciaEmpleadoDTO(
                id = 102,
                titulo = "No enciende el ordenador",
                descripcion = "El ordenador no da señal de vídeo, el monitor se queda en negro...",
                estado = "EN PROGRESO"
            )
        )
    }
}

@Preview(showBackground = true, widthDp = 390)
@Composable
private fun ComentariosIncidenciaEmpleadoComponentPreview() {
    HelpdeskProTheme(dynamicColor = false) {
        ComentariosIncidenciaEmpleadoComponent(
            comentarios = listOf(
                ComentarioIncidenciaDTO(autor = "Técnico", texto = "prueba otro cable HDMI"),
                ComentarioIncidenciaDTO(autor = "Yo", texto = "probado, sigue sin dar vídeo")
            ),
            comentario = "",
            comentarioError = null,
            isSending = false,
            onComentarioChange = {},
            onEnviarComentario = {}
        )
    }
}

@Preview(showBackground = true, widthDp = 390)
@Composable
private fun HistorialIncidenciaEmpleadoComponentPreview() {
    HelpdeskProTheme(dynamicColor = false) {
        HistorialIncidenciaEmpleadoComponent(
            historial = listOf(
                HistorialIncidenciaDTO(fecha = "15/10", descripcion = "Creada por Ana (ABIERTA)"),
                HistorialIncidenciaDTO(fecha = "16/10", descripcion = "Asignada a Luis"),
                HistorialIncidenciaDTO(fecha = "17/10", descripcion = "Estado → EN PROGRESO")
            )
        )
    }
}

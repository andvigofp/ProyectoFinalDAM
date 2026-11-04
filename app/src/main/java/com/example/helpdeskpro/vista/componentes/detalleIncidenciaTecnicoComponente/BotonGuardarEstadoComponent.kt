package com.example.helpdeskpro.vista.componentes.detalleIncidenciaTecnicoComponente

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.helpdeskpro.R
import com.example.helpdeskpro.ui.theme.HelpdeskProTheme


/**
 * Botón para confirmar el estado seleccionado de una incidencia.
 *
 * @param onGuardar callback al solicitar el guardado del estado.
 * @param modifier modificador aplicado al botón.
 * @param isLoading indica si el guardado está en curso.
 * @param enabled indica si el botón está habilitado.
 */
@Composable
fun BotonGuardarEstadoComponent(
    onGuardar: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    enabled: Boolean = true
) {
    BotonAccionDetalleIncidencia(
        texto = stringResource(R.string.save_status),
        textoCargando = stringResource(R.string.saving),
        onClick = onGuardar,
        modifier = modifier,
        isLoading = isLoading,
        enabled = enabled
    )
}

/**
 * Presenta un botón de acción de detalle con el estado de carga.
 *
 * @param texto texto normal del botón.
 * @param textoCargando texto mostrado durante la carga.
 * @param onClick callback al pulsar el botón.
 * @param modifier modificador aplicado al botón.
 * @param isLoading indica si la acción está en curso.
 * @param enabled indica si el botón está habilitado.
 */
@Composable
private fun BotonAccionDetalleIncidencia(
    texto: String,
    textoCargando: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth(0.58f)
            .height(52.dp),
        enabled = enabled && !isLoading,
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF8191AD),
            contentColor = Color.White
        )
    ) {
        Text(
            text = if (isLoading) textoCargando else texto,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            lineHeight = 20.sp
        )
    }
}

@Preview(showBackground = true, widthDp = 390)
@Composable
private fun BotonGuardarEstadoComponentPreview() {
    HelpdeskProTheme(dynamicColor = false) {
        BotonGuardarEstadoComponent(
            onGuardar = {}
        )
    }
}

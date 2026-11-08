package com.example.helpdeskpro.vista.componentes.detalleIncidenciaTecnicoAdminComponente

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.helpdeskpro.R
import com.example.helpdeskpro.modelo.ButtonData
import com.example.helpdeskpro.ui.theme.HelpdeskProTheme
import com.example.helpdeskpro.vista.componentes.buttonsComponente.AppButton


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
        textoResource = R.string.save_status,
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
    textoResource: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    enabled: Boolean = true
) {
    AppButton(
        data = ButtonData(
            nombre = if (isLoading) R.string.saving else textoResource,
            enabled = enabled && !isLoading
        ),
        onClick = onClick,
        modifier = modifier.fillMaxWidth(0.58f).height(52.dp)
    )
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

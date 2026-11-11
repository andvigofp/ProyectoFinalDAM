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
 * Botón para registrar la solución aplicada a una incidencia.
 *
 * @param onRegistrar callback al solicitar el registro de la solución.
 * @param modifier modificador aplicado al botón.
 * @param isLoading indica si el registro está en curso.
 * @param enabled indica si el botón está habilitado.
 */
@Composable
fun BotonRegistrarSolucionComponent(
    onRegistrar: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    enabled: Boolean = true
) {
    AppButton(
        data = ButtonData(
            nombre = if (isLoading) R.string.saving else R.string.register_solution,
            enabled = enabled && !isLoading
        ),
        onClick = onRegistrar,
        modifier = modifier.fillMaxWidth(0.66f).height(52.dp)
    )
}

@Preview(showBackground = true, widthDp = 390)
@Composable
private fun BotonRegistrarSolucionComponentPreview() {
    HelpdeskProTheme(dynamicColor = false) {
        BotonRegistrarSolucionComponent(
            onRegistrar = {}
        )
    }
}

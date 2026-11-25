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
 * Botón para asignar el técnico seleccionado a una incidencia.
 *
 * @param onAsignar callback al solicitar la asignación del técnico.
 * @param modifier modificador aplicado al botón.
 * @param isLoading indica si la asignación está en curso.
 * @param enabled indica si el botón está habilitado.
 */
@Composable
fun BotonAsignarTecnicoComponent(
    onAsignar: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    enabled: Boolean = true
) {
    AppButton(
        data = ButtonData(
            nombre = if (isLoading) R.string.saving else R.string.assign_technician,
            enabled = enabled && !isLoading
        ),
        onClick = onAsignar,
        modifier = modifier.fillMaxWidth(0.58f).height(52.dp)
    )
}

@Preview(showBackground = true, widthDp = 390)
@Composable
private fun BotonAsignarTecnicoComponentPreview() {
    HelpdeskProTheme(dynamicColor = false) {
        BotonAsignarTecnicoComponent(
            onAsignar = {}
        )
    }
}

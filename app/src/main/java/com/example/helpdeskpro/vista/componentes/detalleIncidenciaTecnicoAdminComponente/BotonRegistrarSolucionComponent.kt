package com.example.helpdeskpro.vista.componentes.detalleIncidenciaTecnicoAdminComponente

import androidx.compose.material3.MaterialTheme

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
    Button(
        onClick = onRegistrar,
        modifier = modifier
            .fillMaxWidth(0.66f)
            .height(52.dp),
        enabled = enabled && !isLoading,
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
    ) {
        Text(
            text = if (isLoading) {
                stringResource(R.string.saving)
            } else {
                stringResource(R.string.register_solution)
            },
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            lineHeight = 20.sp
        )
    }
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

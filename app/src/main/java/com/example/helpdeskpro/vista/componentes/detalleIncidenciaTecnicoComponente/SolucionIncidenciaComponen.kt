package com.example.helpdeskpro.vista.componentes.detalleIncidenciaTecnicoComponente

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
 * Campo de texto para introducir la solución aplicada a una incidencia.
 *
 * @param solucion texto de solución actual.
 * @param onSolucionChange callback cuando cambia el texto.
 * @param modifier modificador aplicado al campo.
 * @param enabled indica si el campo está habilitado.
 * @param errorMessage mensaje de validación que se muestra, si existe.
 */
@Composable
fun SolucionIncidenciaComponent(
    solucion: String,
    onSolucionChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    errorMessage: String? = null
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = stringResource(R.string.applied_solution),
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = 14.sp,
                lineHeight = 20.sp
            ),
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF414A5A)
        )

        OutlinedTextField(
            value = solucion,
            onValueChange = onSolucionChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            placeholder = {
                Text(
                    text = stringResource(R.string.solution_placeholder),
                    color = Color(0xFF8796AD),
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )
            },
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 14.sp,
                lineHeight = 20.sp,
                color = Color(0xFF3D4A5E)
            ),
            enabled = enabled,
            isError = errorMessage != null,
            supportingText = {
                if (errorMessage != null) {
                    Text(errorMessage)
                }
            },
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFAAB8CC),
                unfocusedBorderColor = Color(0xFFAAB8CC),
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledBorderColor = Color(0xFFAAB8CC),
                disabledContainerColor = Color.White,
                focusedTextColor = Color(0xFF3D4A5E),
                unfocusedTextColor = Color(0xFF3D4A5E),
                disabledTextColor = Color(0xFF3D4A5E),
                cursorColor = Color(0xFF596F91)
            )
        )
    }
}

@Preview(showBackground = true, widthDp = 390)
@Composable
private fun SolucionIncidenciaComponentPreview() {
    HelpdeskProTheme(dynamicColor = false) {
        SolucionIncidenciaComponent(
            solucion = "",
            onSolucionChange = {}
        )
    }
}

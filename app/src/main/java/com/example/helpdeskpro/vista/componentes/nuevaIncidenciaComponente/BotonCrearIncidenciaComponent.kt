package com.example.helpdeskpro.vista.componentes.nuevaIncidenciaComponente

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import com.example.helpdeskpro.R
import com.example.helpdeskpro.ui.theme.HelpdeskProTheme

private val NuevaIncidenciaButtonColor = Color(0xFF8998AD)

@Composable
fun BotonCrearIncidenciaComponent(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false
) {
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth().height(52.dp),
        enabled = !isLoading,
        colors = ButtonDefaults.buttonColors(containerColor = NuevaIncidenciaButtonColor)
    ) {
        Text(
            text = stringResource(if (isLoading) R.string.creating else R.string.create_incident),
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
private fun BotonCrearIncidenciaComponentPreview() {
    HelpdeskProTheme(dynamicColor = false) {
        BotonCrearIncidenciaComponent(onClick = {})
    }
}

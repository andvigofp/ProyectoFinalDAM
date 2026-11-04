package com.example.helpdeskpro.vista.componentes.nuevaIncidenciaComponente

import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.helpdeskpro.ui.theme.HelpdeskProTheme

@Composable
fun BotonVolverNuevaIncidenciaComponent(
    onClick: () -> Unit,
    enabled: Boolean = true
) {
    IconButton(onClick = onClick, enabled = enabled) {
        Text(
            text = "‹",
            style = MaterialTheme.typography.headlineMedium
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BotonVolverNuevaIncidenciaComponentPreview() {
    HelpdeskProTheme(dynamicColor = false) {
        BotonVolverNuevaIncidenciaComponent(onClick = {})
    }
}

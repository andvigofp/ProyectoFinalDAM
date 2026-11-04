package com.example.helpdeskpro.vista.componentes.crearCuentaComponente

import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.helpdeskpro.ui.theme.HelpdeskProTheme

@Composable
fun BotonVolverCrearCuentaComponent(
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
private fun BotonVolverCrearCuentaComponentPreview() {
    HelpdeskProTheme(dynamicColor = false) {
        BotonVolverCrearCuentaComponent(onClick = {})
    }
}

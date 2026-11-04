package com.example.helpdeskpro.vista.componentes.homeEmpleadoComponete

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.helpdeskpro.ui.theme.HelpdeskProTheme

@Composable
fun BotonNuevaIncidenciaFlotanteComponent(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier,
        shape = CircleShape,
        containerColor = Color(0xFF637A99),
        contentColor = Color.White
    ) {
        Text(
            text = "+",
            style = MaterialTheme.typography.headlineMedium
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BotonNuevaIncidenciaFlotanteComponentPreview() {
    HelpdeskProTheme(dynamicColor = false) {
        BotonNuevaIncidenciaFlotanteComponent(onClick = {})
    }
}

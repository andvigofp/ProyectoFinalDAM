package com.example.helpdeskpro.vista.componentes.homeAdminComponente

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.helpdeskpro.modelo.HomeAdminDTO

@Composable
fun ResumenIncidenciasAdminComponent(
    resumen: HomeAdminDTO,
    etiquetas: List<String>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        TarjetaResumenAdminComponent(
            cantidad = resumen.abiertas,
            etiqueta = etiquetas[0],
            modifier = Modifier.weight(1f)
        )
        TarjetaResumenAdminComponent(
            cantidad = resumen.enProgreso,
            etiqueta = etiquetas[1],
            modifier = Modifier.weight(1f)
        )
        TarjetaResumenAdminComponent(
            cantidad = resumen.resueltas,
            etiqueta = etiquetas[2],
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun TarjetaResumenAdminComponent(
    cantidad: Int,
    etiqueta: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surface,
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(modifier = Modifier.padding(vertical = 18.dp, horizontal = 4.dp)) {
            Text(
                text = cantidad.toString(),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = etiqueta,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

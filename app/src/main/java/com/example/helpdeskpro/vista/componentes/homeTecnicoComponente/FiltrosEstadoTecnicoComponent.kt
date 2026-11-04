package com.example.helpdeskpro.vista.componentes.homeTecnicoComponente

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import com.example.helpdeskpro.ui.theme.HelpdeskProTheme

private data class FiltroEstado(
    val estado: String,
    val etiqueta: String
)

@Composable
fun FiltrosEstadoTecnicoComponent(
    filtroSeleccionado: String?,
    onFiltroSeleccionado: (String?) -> Unit,
    modifier: Modifier = Modifier,
    etiquetas: List<String>
) {
    val filtrosEstado = listOf(
        FiltroEstado("ABIERTA", etiquetas[0]),
        FiltroEstado("EN PROGRESO", etiquetas[1]),
        FiltroEstado("RESUELTA", etiquetas[2])
    )

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        filtrosEstado.forEach { filtro ->
            val selected = filtroSeleccionado?.normalizarEstado() == filtro.estado.normalizarEstado()
            Surface(
                modifier = Modifier
                    .height(28.dp)
                    .clickable {
                        onFiltroSeleccionado(if (selected) null else filtro.estado)
                    },
                shape = RoundedCornerShape(18.dp),
                color = if (selected) Color(0xFFDCE4EF) else Color(0xFFF3F5F7),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (selected) Color(0xFF637A99) else Color(0xFFAAB8CC)
                )
            ) {
                Text(
                    text = filtro.etiqueta,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                    color = Color(0xFF414A5A),
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        }
    }
}

private fun String.normalizarEstado(): String =
    trim().uppercase().replace('_', ' ').replace(Regex("\\s+"), " ")

@Preview(showBackground = true, widthDp = 390)
@Composable
private fun FiltrosEstadoTecnicoComponentPreview() {
    HelpdeskProTheme(dynamicColor = false) {
        FiltrosEstadoTecnicoComponent(
            filtroSeleccionado = null,
            onFiltroSeleccionado = {},
            modifier = Modifier.padding(8.dp),
            etiquetas = listOf("Abiertas", "En progreso", "Resueltas")
        )
    }
}

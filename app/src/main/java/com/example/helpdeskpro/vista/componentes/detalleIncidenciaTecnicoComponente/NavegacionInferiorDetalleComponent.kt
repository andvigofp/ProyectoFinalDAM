package com.example.helpdeskpro.vista.componentes.detalleIncidenciaTecnicoComponente

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.helpdeskpro.R
import com.example.helpdeskpro.ui.theme.HelpdeskProTheme

/**
 * Barra inferior de navegación entre incidencias y perfil.
 *
 * @param onIncidencias callback para abrir el listado de incidencias.
 * @param onPerfil callback para abrir el perfil.
 * @param modifier modificador aplicado a la barra.
 */
@Composable
fun NavegacionInferiorDetalleComponent(
    onIncidencias: () -> Unit,
    onPerfil: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(66.dp)
            .background(Color(0xFFE8ECF0)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        NavegacionInferiorDetalleItem(
            label = stringResource(R.string.incidents),
            selected = true,
            onClick = onIncidencias,
            modifier = Modifier.weight(1f)
        )
        NavegacionInferiorDetalleItem(
            label = stringResource(R.string.profile),
            selected = false,
            onClick = onPerfil,
            modifier = Modifier.weight(1f)
        )
    }
}

/**
 * Elemento de una pestaña de la navegación inferior del detalle.
 *
 * @param label texto mostrado bajo el indicador.
 * @param selected indica si el elemento está seleccionado.
 * @param onClick callback al pulsar el elemento.
 * @param modifier modificador aplicado al elemento.
 */
@Composable
private fun NavegacionInferiorDetalleItem(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(
                    color = if (selected) Color(0xFF596F91) else Color(0xFFD0D6DE),
                    shape = CircleShape
                )
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 12.sp,
                lineHeight = 16.sp
            ),
            color = if (selected) Color(0xFF414A5A) else Color(0xFF8796AD)
        )
    }
}

@Preview(showBackground = true, widthDp = 390)
@Composable
private fun NavegacionInferiorDetalleComponentPreview() {
    HelpdeskProTheme(dynamicColor = false) {
        NavegacionInferiorDetalleComponent(
            onIncidencias = {},
            onPerfil = {}
        )
    }
}

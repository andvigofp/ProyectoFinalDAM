package com.example.helpdeskpro.vista.componentes.toolbarComponenete

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.example.helpdeskpro.R

@Composable
fun ToolbarDetalleIncidenciaComponent(
    onVolver: () -> Unit,
    modifier: Modifier = Modifier
) {
    ToolbarComponent(
        titulo = stringResource(R.string.incident_detail),
        modifier = modifier,
        onVolver = onVolver,
        titleStyle = MaterialTheme.typography.titleLarge.copy(
            fontSize = 20.sp,
            lineHeight = 24.sp
        )
    )
}

package com.example.helpdeskpro.vista.componentes.nuevaIncidenciaComponente

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.helpdeskpro.R
import com.example.helpdeskpro.modelo.ButtonData
import com.example.helpdeskpro.vista.componentes.buttonsComponente.AppButton
import com.example.helpdeskpro.ui.theme.HelpdeskProTheme


@Composable
fun BotonCrearIncidenciaComponent(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false
) {
    AppButton(
        data = ButtonData(
            nombre = if (isLoading) R.string.creating else R.string.create_incident,
            enabled = !isLoading
        ),
        onClick = onClick,
        modifier = modifier.fillMaxWidth().height(52.dp)
    )
}

@Preview(showBackground = true, widthDp = 360)
@Composable
private fun BotonCrearIncidenciaComponentPreview() {
    HelpdeskProTheme(dynamicColor = false) {
        BotonCrearIncidenciaComponent(onClick = {})
    }
}

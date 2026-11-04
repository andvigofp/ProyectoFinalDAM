package com.example.helpdeskpro.vista.componentes.logingComponente

import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.helpdeskpro.R
import com.example.helpdeskpro.ui.theme.HelpdeskProTheme

@Composable
fun BotonVisibilidadContrasenaLoginComponent(
    visible: Boolean,
    onClick: () -> Unit,
    enabled: Boolean = true
) {
    TextButton(onClick = onClick, enabled = enabled) {
        Text(stringResource(if (visible) R.string.hide else R.string.show))
    }
}

@Preview(showBackground = true)
@Composable
private fun BotonVisibilidadContrasenaLoginComponentPreview() {
    HelpdeskProTheme(dynamicColor = false) {
        BotonVisibilidadContrasenaLoginComponent(visible = false, onClick = {})
    }
}

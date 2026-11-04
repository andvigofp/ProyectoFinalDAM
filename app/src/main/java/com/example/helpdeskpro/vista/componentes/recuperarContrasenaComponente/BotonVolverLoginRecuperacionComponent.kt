package com.example.helpdeskpro.vista.componentes.recuperarContrasenaComponente

import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.helpdeskpro.R
import com.example.helpdeskpro.ui.theme.HelpdeskProTheme

@Composable
fun BotonVolverLoginRecuperacionComponent(
    onClick: () -> Unit,
    enabled: Boolean = true
) {
    TextButton(onClick = onClick, enabled = enabled) {
        Text(stringResource(R.string.back_to_login))
    }
}

@Preview(showBackground = true)
@Composable
private fun BotonVolverLoginRecuperacionComponentPreview() {
    HelpdeskProTheme(dynamicColor = false) {
        BotonVolverLoginRecuperacionComponent(onClick = {})
    }
}

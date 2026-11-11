package com.example.helpdeskpro.vista.componentes.recuperarContrasenaComponente

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
fun BotonEnviarRecuperacionComponent(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false
) {
    AppButton(
        data = ButtonData(
            nombre = if (isLoading) R.string.sending else R.string.send_temporary_password,
            enabled = !isLoading
        ),
        onClick = onClick,
        modifier = modifier.fillMaxWidth().height(52.dp)
    )
}

@Preview(showBackground = true, widthDp = 360)
@Composable
private fun BotonEnviarRecuperacionComponentPreview() {
    HelpdeskProTheme(dynamicColor = false) {
        BotonEnviarRecuperacionComponent(onClick = {})
    }
}
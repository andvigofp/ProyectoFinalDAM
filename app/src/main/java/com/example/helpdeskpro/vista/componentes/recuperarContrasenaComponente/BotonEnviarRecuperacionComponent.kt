package com.example.helpdeskpro.vista.componentes.recuperarContrasenaComponente

import androidx.compose.material3.MaterialTheme

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.helpdeskpro.R
import com.example.helpdeskpro.ui.theme.HelpdeskProTheme


@Composable
fun BotonEnviarRecuperacionComponent(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false
) {
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth().height(52.dp),
        enabled = !isLoading,
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
    ) {
        Text(
            text = stringResource(
                if (isLoading) R.string.sending else R.string.send_temporary_password
            ),
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
private fun BotonEnviarRecuperacionComponentPreview() {
    HelpdeskProTheme(dynamicColor = false) {
        BotonEnviarRecuperacionComponent(onClick = {})
    }
}

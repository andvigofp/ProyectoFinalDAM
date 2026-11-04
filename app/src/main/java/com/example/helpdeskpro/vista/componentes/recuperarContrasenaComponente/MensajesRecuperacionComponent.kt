package com.example.helpdeskpro.vista.componentes.recuperarContrasenaComponente

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.helpdeskpro.ui.theme.HelpdeskProTheme

@Composable
fun MensajesRecuperacionComponent(
    errorMessage: String?,
    successMessage: String?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (errorMessage != null) {
            Text(
                text = errorMessage,
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        if (successMessage != null) {
            Text(
                text = successMessage,
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }

}

@Preview(showBackground = true, widthDp = 360)
@Composable
private fun MensajesRecuperacionComponentPreview() {
    HelpdeskProTheme(dynamicColor = false) {
        MensajesRecuperacionComponent(
            errorMessage = null,
            successMessage = null
        )
    }
}

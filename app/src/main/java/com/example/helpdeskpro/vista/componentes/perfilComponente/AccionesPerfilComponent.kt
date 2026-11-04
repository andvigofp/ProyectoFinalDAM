package com.example.helpdeskpro.vista.componentes.perfilComponente

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Arrangement
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
fun AccionesPerfilComponent(
    onEditarPerfil: () -> Unit,
    onCerrarSesion: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Button(
            onClick = onEditarPerfil,
            modifier = Modifier.fillMaxWidth().height(52.dp),
            enabled = !isLoading,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8998AD))
        ) {
            Text(stringResource(R.string.edit_profile), fontWeight = FontWeight.SemiBold)
        }
        Button(
            onClick = onCerrarSesion,
            modifier = Modifier.fillMaxWidth().height(52.dp),
            enabled = !isLoading,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB05A5A))
        ) {
            Text(stringResource(R.string.logout), fontWeight = FontWeight.SemiBold)
        }
    }

}

@Preview(showBackground = true, widthDp = 360)
@Composable
private fun AccionesPerfilComponentPreview() {
    HelpdeskProTheme(dynamicColor = false) {
        AccionesPerfilComponent(onEditarPerfil = {}, onCerrarSesion = {})
    }
}

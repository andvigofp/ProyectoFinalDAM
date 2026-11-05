package com.example.helpdeskpro.vista.componentes.perfilComponente

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.helpdeskpro.R
import com.example.helpdeskpro.modelo.PerfilDTO
import com.example.helpdeskpro.ui.theme.HelpdeskProTheme


@Composable
fun CamposPerfilComponent(perfil: PerfilDTO, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        DatoPerfilComponent(label = stringResource(R.string.email), value = perfil.email)
        DatoPerfilComponent(label = stringResource(R.string.phone), value = perfil.telefono)
        DatoPerfilComponent(label = stringResource(R.string.department), value = perfil.departamento)
        DatoPerfilComponent(label = stringResource(R.string.member_since), value = perfil.miembroDesde)
    }
}

@Composable
fun DatoPerfilComponent(label: String, value: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodySmall
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value.ifBlank { stringResource(R.string.not_provided) },
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
private fun CamposPerfilComponentPreview() {
    HelpdeskProTheme(dynamicColor = false) {
        CamposPerfilComponent(
            perfil = PerfilDTO(
                id = 1,
                nombre = "Ana",
                apellidos = "Pérez",
                email = "ana@empresa.com",
                telefono = "600 123 456",
                departamento = "Ventas",
                miembroDesde = "01/09/2026",
                rol = "EMPLEADO"
            )
        )
    }
}

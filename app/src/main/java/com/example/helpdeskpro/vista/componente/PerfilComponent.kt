package com.example.helpdeskpro.vista.componente

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.helpdeskpro.R
import com.example.helpdeskpro.modelo.PerfilDTO
import com.example.helpdeskpro.ui.theme.HelpdeskProTheme

private val PerfilSecondaryTextColor = Color(0xFF8A8F98)

/** Contenido reutilizable con los datos y acciones del perfil. */
@Composable
fun PerfilComponent(
    perfil: PerfilDTO,
    onEditarPerfil: () -> Unit,
    onCerrarSesion: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false
) {
    val iniciales = listOf(perfil.nombre, perfil.apellidos)
        .mapNotNull { valor -> valor.trim().firstOrNull() }
        .take(2)
        .joinToString("")
        .uppercase()
        .ifBlank { "?" }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            modifier = Modifier.size(96.dp),
            shape = CircleShape,
            color = Color(0xFFD7DEE8)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
            ) {
                Text(
                    text = iniciales,
                    color = Color(0xFF4E5F78),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.full_name, perfil.nombre, perfil.apellidos).trim(),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        Surface(
            shape = CircleShape,
            color = Color(0xFF637A99),
            contentColor = Color.White
        ) {
            Text(
                text = perfil.rol.replace('_', ' ').uppercase(),
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                style = MaterialTheme.typography.labelSmall
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        Column(modifier = Modifier.fillMaxWidth()) {
            DatoPerfil(label = stringResource(R.string.email), value = perfil.email)
            DatoPerfil(label = stringResource(R.string.phone), value = perfil.telefono)
            DatoPerfil(label = stringResource(R.string.department), value = perfil.departamento)
            DatoPerfil(label = stringResource(R.string.member_since), value = perfil.miembroDesde)
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onEditarPerfil,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            enabled = !isLoading,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8998AD))
        ) {
            Text(
                text = stringResource(R.string.edit_profile),
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onCerrarSesion,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            enabled = !isLoading,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB05A5A))
        ) {
            Text(
                text = stringResource(R.string.logout),
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun DatoPerfil(label: String, value: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            color = PerfilSecondaryTextColor,
            style = MaterialTheme.typography.bodySmall
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = if (value.isBlank()) stringResource(R.string.not_provided) else value,
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(color = Color(0xFFE1E3E6))
        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun PerfilComponentPreview() {
    HelpdeskProTheme(dynamicColor = false) {
        PerfilComponent(
            perfil = PerfilDTO(
                id = 1,
                nombre = "Ana",
                apellidos = "Pérez",
                email = "ana@empresa.com",
                telefono = "600 123 456",
                departamento = "Ventas",
                miembroDesde = "01/09/2026",
                rol = "EMPLEADO"
            ),
            onEditarPerfil = {},
            onCerrarSesion = {}
        )
    }
}

package com.example.helpdeskpro.vista.componentes.perfilComponente

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
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

/** Contenido reutilizable con los datos y acciones del perfil. */
@Composable
fun PerfilComponent(
    perfil: PerfilDTO,
    onEditarPerfil: () -> Unit,
    onCerrarSesion: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AvatarPerfilComponent(nombre = perfil.nombre, apellidos = perfil.apellidos)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.full_name, perfil.nombre, perfil.apellidos).trim(),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(12.dp))
        Surface(shape = CircleShape, color = Color(0xFF637A99), contentColor = Color.White) {
            Text(
                text = perfil.rol.replace('_', ' ').uppercase(),
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                style = MaterialTheme.typography.labelSmall
            )
        }
        Spacer(modifier = Modifier.height(28.dp))
        DatosPerfilComponent(perfil = perfil)
        Spacer(modifier = Modifier.height(32.dp))
        AccionesPerfilComponent(
            onEditarPerfil = onEditarPerfil,
            onCerrarSesion = onCerrarSesion,
            isLoading = isLoading
        )
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
